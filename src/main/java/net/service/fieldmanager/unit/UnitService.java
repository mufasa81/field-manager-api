package net.service.fieldmanager.unit;

import lombok.RequiredArgsConstructor;
import net.service.fieldmanager.building.Building;
import net.service.fieldmanager.building.BuildingRepository;
import net.service.fieldmanager.building.Unit;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final BuildingRepository buildingRepository;

    public void updateUnit(String buildingId, String unitId, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        Optional<Building> buildingOptional = buildingRepository.findById(buildingId);
        if (buildingOptional.isPresent()) {
            Building building = buildingOptional.get();
            Optional<Unit> unitOptional = building.getUnits().stream().filter(u -> u.getId().equals(unitId)).findFirst();

            if (unitOptional.isPresent()) {
                Unit unit = unitOptional.get();
                updates.forEach((key, value) -> {
                    if ("isDoNotVisit".equals(key)) {
                        unit.setDoNotVisit((Boolean) value);
                    }
                    if ("value".equals(key)) {
                        unit.setValue((String) value);
                    }
                });
                buildingRepository.save(building);
            } else {
                throw new IllegalArgumentException("Unit not found");
            }
        } else {
            throw new IllegalArgumentException("Building not found");
        }
    }
}
