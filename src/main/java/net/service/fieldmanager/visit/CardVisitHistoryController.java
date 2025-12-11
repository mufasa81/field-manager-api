package net.service.fieldmanager.visit;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CardVisitHistoryController {

    private final CardVisitHistoryService visitHistoryService;

    @PostMapping("/visit-history")
    public ResponseEntity<?> createVisitHistory(@RequestBody CardVisitHistory visitHistory) {
        try {
            CardVisitHistory createdHistory = visitHistoryService.createVisitHistory(visitHistory);
            return new ResponseEntity<>(createdHistory, HttpStatus.CREATED);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/areas/{areaId}/visit-history")
    public ResponseEntity<?> getVisitHistoryByArea(@PathVariable String areaId) {
        try {
            List<CardVisitHistory> history = visitHistoryService.getVisitHistoryByArea(areaId);
            return new ResponseEntity<>(history, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buildings/{buildingId}/visit-history")
    public ResponseEntity<?> getVisitHistoryByBuilding(@PathVariable String buildingId) {
        try {
            List<CardVisitHistory> history = visitHistoryService.getVisitHistoryByBuilding(buildingId);
            return new ResponseEntity<>(history, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/visit-history/{id}")
    public ResponseEntity<?> deleteVisitHistory(@PathVariable String id) {
        try {
            visitHistoryService.deleteVisitHistory(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
