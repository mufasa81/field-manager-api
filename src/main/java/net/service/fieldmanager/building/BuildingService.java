package net.service.fieldmanager.building;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class BuildingService {

    private final BuildingRepository buildingRepository;

    public Building createBuilding(Building building) throws ExecutionException, InterruptedException {
        return buildingRepository.save(building);
    }

    public Building getBuildingById(String id) throws ExecutionException, InterruptedException {
        return buildingRepository.findById(id).orElse(null);
    }

    public Building updateBuilding(String id, Building building) throws ExecutionException, InterruptedException {
        building.setId(id);
        return buildingRepository.save(building);
    }

    public void deleteBuilding(String id) throws ExecutionException, InterruptedException {
        buildingRepository.deleteById(id);
    }

    public List<Building> getBuildingsByAreaId(String areaId) throws ExecutionException, InterruptedException {
        return buildingRepository.findAllByAreaId(areaId);
    }
}

