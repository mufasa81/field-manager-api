package net.service.fieldmanager.area;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class AreaService {

    private final AreaRepository areaRepository;

    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    public Area createArea(Area area) throws ExecutionException, InterruptedException {
        return areaRepository.save(area);
    }

    public Area getAreaById(String id) throws ExecutionException, InterruptedException {
        return areaRepository.findById(id).orElse(null);
    }

    public Area updateArea(String id, Area area) throws ExecutionException, InterruptedException {
        area.setId(id);
        return areaRepository.save(area);
    }

    public void deleteArea(String id) throws ExecutionException, InterruptedException {
        areaRepository.deleteById(id);
    }

    public List<Area> getAreasByAreaGroupId(String areaGroupId) throws ExecutionException, InterruptedException {
        return areaRepository.findAllByAreaGroupId(areaGroupId);
    }

    public List<Area> getAllAreas() throws ExecutionException, InterruptedException {
        return areaRepository.findAll();
    }
}
