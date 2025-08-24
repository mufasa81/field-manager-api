package net.service.fieldmanager.building;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BuildingController {

    private static final Logger logger = LoggerFactory.getLogger(BuildingController.class);

    private final BuildingService buildingService;

    @PostMapping("/buildings")
    public ResponseEntity<Building> createBuilding(@RequestBody Building building) {
        try {
            Building createdBuilding = buildingService.createBuilding(building);
            return new ResponseEntity<>(createdBuilding, HttpStatus.CREATED);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buildings/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable String id) {
        try {
            Building building = buildingService.getBuildingById(id);
            if (building != null) {
                return new ResponseEntity<>(building, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/buildings/{id}")
    public ResponseEntity<Building> updateBuilding(@PathVariable String id, @RequestBody Building building) {
        logger.info("Received update request for building ID: {}", id);
        logger.info("Building object received: {}", building);
        try {
            Building updatedBuilding = buildingService.updateBuilding(id, building);
            if (updatedBuilding != null) {
                return new ResponseEntity<>(updatedBuilding, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error updating building with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/buildings/{id}")
    public ResponseEntity<HttpStatus> deleteBuilding(@PathVariable String id) {
        try {
            buildingService.deleteBuilding(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/areas/{areaId}/buildings")
    public ResponseEntity<List<Building>> getBuildingsByAreaId(@PathVariable String areaId) {
        try {
            List<Building> buildings = buildingService.getBuildingsByAreaId(areaId);
            return new ResponseEntity<>(buildings, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
