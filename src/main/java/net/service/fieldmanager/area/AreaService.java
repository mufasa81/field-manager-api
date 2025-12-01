package net.service.fieldmanager.area;

import lombok.RequiredArgsConstructor;
import net.service.fieldmanager.building.Building;
import net.service.fieldmanager.building.BuildingRepository;
import net.service.fieldmanager.building.Unit;
import net.service.fieldmanager.visit.CardVisitHistory;
import net.service.fieldmanager.visit.CardVisitHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AreaService {

    private final AreaRepository areaRepository;
    private final BuildingRepository buildingRepository;
    private final CardVisitHistoryService cardVisitHistoryService;
    private static final Logger logger = LoggerFactory.getLogger(AreaService.class);

    public Area createArea(Area area) throws ExecutionException, InterruptedException {
        // When creating, we can assume hasDoNotVisit is false or calculate it if buildings are linked
        area.setHasDoNotVisit(false); // Default to false on creation
        area.setCompletedCount(0); // Default to 0 on creation
        return areaRepository.save(area);
    }

    public Area getAreaById(String id) throws ExecutionException, InterruptedException {
        Area area = areaRepository.findById(id).orElse(null);
        if (area != null) {
            updateHasDoNotVisitFlag(area);
            updateCompletedCount(area);
        }
        return area;
    }

    public Area updateArea(String id, Area area) throws ExecutionException, InterruptedException {
        area.setId(id);
        // Recalculate hasDoNotVisit on every update, as child buildings/units might have changed.
        updateHasDoNotVisitFlag(area);
        // Completed count is dynamic, but we might want to reset the stored value if needed.
        // For now, we let the client handle what it sends in the Area object for this field.
        return areaRepository.save(area);
    }

    public void deleteArea(String id) throws ExecutionException, InterruptedException {
        areaRepository.deleteById(id);
    }

    public List<Area> getAreasByAreaGroupId(String areaGroupId) throws ExecutionException, InterruptedException {
        List<Area> areas = areaRepository.findAllByAreaGroupId(areaGroupId);
        for (Area area : areas) {
            // Also update the do-not-visit flag here
            updateHasDoNotVisitFlag(area);
            updateCompletedCount(area);
            int buildingCount = buildingRepository.findAllByAreaId(area.getId()).size();
            area.setBuildingCount(buildingCount);
        }
        return areas;
    }

    public List<Area> getAllAreas() throws ExecutionException, InterruptedException {
        List<Area> areas = areaRepository.findAll();
        // Update the flag for all areas
        for (Area area : areas) {
            updateHasDoNotVisitFlag(area);
            updateCompletedCount(area);
        }
        return areas;
    }

    private void updateHasDoNotVisitFlag(Area area) {
        try {
            List<Building> buildings = buildingRepository.findAllByAreaId(area.getId());
            boolean hasDoNotVisit = buildings.stream()
                    .anyMatch(building -> building.getUnits() != null && building.getUnits().stream()
                            .anyMatch(Unit::isDoNotVisit));
            area.setHasDoNotVisit(hasDoNotVisit);
        } catch (Exception e) {
            logger.error("Error updating hasDoNotVisit flag for area " + area.getId(), e);
        }
    }

    private void updateCompletedCount(Area area) {
        try {
            LocalDate today = LocalDate.now();
            int currentYear = today.getYear();
            // Service year starts on September 1st.
            LocalDate serviceYearStart;
            if (today.getMonth().getValue() < Month.SEPTEMBER.getValue()) {
                // If we are in Jan-Aug, the service year started last year.
                serviceYearStart = LocalDate.of(currentYear - 1, Month.SEPTEMBER, 1);
            } else {
                // If we are in Sep-Dec, the service year started this year.
                serviceYearStart = LocalDate.of(currentYear, Month.SEPTEMBER, 1);
            }
            Instant serviceYearStartInstant = serviceYearStart.atStartOfDay().toInstant(ZoneOffset.UTC);

            List<CardVisitHistory> histories = cardVisitHistoryService.getVisitHistoryByArea(area.getId());

            long count = histories.stream()
                .filter(h -> h.getVisitDate() != null && h.getVisitDate().isAfter(serviceYearStartInstant))
                .map(h -> h.getVisitDate().truncatedTo(ChronoUnit.DAYS)) // Map to date part only
                .distinct() // Count unique dates
                .count();

            area.setCompletedCount((int) count);

        } catch (Exception e) {
            logger.error("Error updating completed count for area " + area.getId(), e);
            area.setCompletedCount(0); // Default to 0 on error
        }
    }

    public boolean isAreaCompleted(Area area) {
        try {
            List<Building> buildings = buildingRepository.findAllByAreaId(area.getId());
            if (buildings.isEmpty()) {
                return false; // An area with no buildings cannot be completed.
            }

            List<CardVisitHistory> areaHistories = cardVisitHistoryService.getVisitHistoryByArea(area.getId());

            // Check if all buildings in the area are completed.
            return buildings.stream().allMatch(building -> isBuildingCompleted(building, areaHistories));

        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error checking completion status for Area ID: {}", area.getId(), e);
            Thread.currentThread().interrupt();
            return false;
        }
    }
    private boolean isBuildingCompleted(Building building, List<CardVisitHistory> allAreaHistories) {
        // Filter histories relevant to the current building for efficiency
        List<CardVisitHistory> buildingHistories = allAreaHistories.stream()
                .filter(h -> building.getId().equals(h.getBuildingId()))
                .collect(Collectors.toList());

        // Condition 1: Is there a building-level "ALL_ABSENT" record?
        boolean isAllAbsent = buildingHistories.stream()
                .anyMatch(h -> "ALL_ABSENT".equals(h.getVisitType()));
        if (isAllAbsent) {
            return true;
        }

        // Condition 2: Has any unit in this building been visited?
        if (building.getUnits() == null || building.getUnits().isEmpty()) {
            return false; // No units, and not marked as ALL_ABSENT.
        }
        Set<String> unitIdsInBuilding = building.getUnits().stream().map(Unit::getId).collect(Collectors.toSet());

        // Check if any visit history record corresponds to any unit in this building.
        boolean anyUnitVisited = allAreaHistories.stream()
                .anyMatch(h -> h.getUnitId() != null && unitIdsInBuilding.contains(h.getUnitId()));

        return anyUnitVisited;
    }
}
