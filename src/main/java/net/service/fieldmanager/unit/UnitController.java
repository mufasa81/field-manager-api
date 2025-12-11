package net.service.fieldmanager.unit;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UnitController {

    private final UnitService unitService;

    @PatchMapping("/buildings/{buildingId}/units/{unitId}")
    public ResponseEntity<Void> updateUnit(
            @PathVariable String buildingId,
            @PathVariable String unitId,
            @RequestBody Map<String, Object> updates) {
        try {
            unitService.updateUnit(buildingId, unitId, updates);
            return ResponseEntity.ok().build();
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
