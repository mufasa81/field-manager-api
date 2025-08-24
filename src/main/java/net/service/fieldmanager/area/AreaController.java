package net.service.fieldmanager.area;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class AreaController {

    private final AreaService areaService;

    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @PostMapping("/areas")
    public ResponseEntity<Area> createArea(@RequestBody Area area) {
        try {
            Area createdArea = areaService.createArea(area);
            return new ResponseEntity<>(createdArea, HttpStatus.CREATED);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/areas/{id}")
    public ResponseEntity<Area> getAreaById(@PathVariable String id) {
        try {
            Area area = areaService.getAreaById(id);
            if (area != null) {
                return new ResponseEntity<>(area, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/areas")
    public ResponseEntity<List<Area>> getAllAreas() {
        try {
            List<Area> areas = areaService.getAllAreas();
            return new ResponseEntity<>(areas, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/areas/{id}")
    public ResponseEntity<Area> updateArea(@PathVariable String id, @RequestBody Area area) {
        try {
            Area updatedArea = areaService.updateArea(id, area);
            if (updatedArea != null) {
                return new ResponseEntity<>(updatedArea, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/areas/{id}")
    public ResponseEntity<HttpStatus> deleteArea(@PathVariable String id) {
        try {
            areaService.deleteArea(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/area-groups/{areaGroupId}/areas")
    public ResponseEntity<List<Area>> getAreasByAreaGroupId(@PathVariable String areaGroupId) {
        try {
            List<Area> areas = areaService.getAreasByAreaGroupId(areaGroupId);
            return new ResponseEntity<>(areas, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
