package net.service.fieldmanager.areagroup;

import lombok.RequiredArgsConstructor;
import net.service.fieldmanager.area.Area;
import net.service.fieldmanager.area.AreaRepository;
import net.service.fieldmanager.area.AreaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AreaGroupService {

    private final AreaGroupRepository areaGroupRepository;
    private final AreaRepository areaRepository;
    private final AreaService areaService;

    public AreaGroup createAreaGroup(AreaGroupCreationRequest request) throws ExecutionException, InterruptedException {
        AreaGroup areaGroup = new AreaGroup();
        areaGroup.setName(request.getName());
        areaGroup.setTotalAreaCards(request.getTotalAreaCards());
        areaGroup.setCompletedCards(0); // Default to 0 on creation
        areaGroup.setTotalHouseholds(request.getTotalHouseholds());
        areaGroup.setDescription(request.getDescription());
        areaGroup.setMapCoordinates(request.getMapCoordinates());
        return areaGroupRepository.save(areaGroup);
    }

    public Optional<AreaGroup> getAreaGroupById(String id) throws ExecutionException, InterruptedException {
        Optional<AreaGroup> areaGroupOpt = areaGroupRepository.findById(id);
        if (areaGroupOpt.isPresent()) {
            AreaGroup areaGroup = areaGroupOpt.get();
            try {
                // Also calculate completed cards here
                List<Area> areas = areaRepository.findAllByAreaGroupId(areaGroup.getId());
                long completedCount = areas.stream().filter(areaService::isAreaCompleted).count();
                areaGroup.setAreaCount(areas.size());
                areaGroup.setCompletedCards((int) completedCount);
            } catch (ExecutionException | InterruptedException e) {
                // 예외 처리
                areaGroup.setAreaCount(0);
                areaGroup.setCompletedCards(0);
            }
        }
        return areaGroupOpt;
    }

    public List<AreaGroup> getAllAreaGroups() throws ExecutionException, InterruptedException {
        List<AreaGroup> areaGroups = areaGroupRepository.findAll();
        return areaGroups.stream().peek(areaGroup -> {
            try {
                List<Area> areas = areaRepository.findAllByAreaGroupId(areaGroup.getId());
                long completedCount = areas.stream().filter(areaService::isAreaCompleted).count();
                areaGroup.setAreaCount(areas.size());
                areaGroup.setCompletedCards((int) completedCount);
            } catch (ExecutionException | InterruptedException e) {
                // 예외 처리, 예를 들어 로깅 또는 기본값 설정
                areaGroup.setAreaCount(0);
                areaGroup.setCompletedCards(0);
            }
        }).collect(Collectors.toList());
    }

    public AreaGroup updateAreaGroup(String id, AreaGroupUpdateRequest request) throws ExecutionException, InterruptedException {
        AreaGroup areaGroup = areaGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AreaGroup not found with id: " + id));

        if (request.getName() != null) {
            areaGroup.setName(request.getName());
        }
        if (request.getTotalAreaCards() != null) {
            areaGroup.setTotalAreaCards(request.getTotalAreaCards());
        }
        if (request.getCompletedCards() != null) {
            areaGroup.setCompletedCards(request.getCompletedCards());
        }
        if (request.getTotalHouseholds() != 0) { // 0이 유효한 값일 경우 로직 변경 필요
            areaGroup.setTotalHouseholds(request.getTotalHouseholds());
        }
        if (request.getDescription() != null) {
            areaGroup.setDescription(request.getDescription());
        }
        if (request.getMapCoordinates() != null) {
            areaGroup.setMapCoordinates(request.getMapCoordinates());
        }

        return areaGroupRepository.save(areaGroup);
    }

    public void deleteAreaGroup(String id) {
        areaGroupRepository.deleteById(id);
    }
}
