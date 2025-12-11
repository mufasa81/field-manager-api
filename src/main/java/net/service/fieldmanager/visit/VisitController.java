package net.service.fieldmanager.visit;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public ResponseEntity<Visit> recordVisit(@RequestBody VisitRequest request) throws ExecutionException, InterruptedException {
        Visit visit = visitService.recordVisit(request.areaId(), request.buildingId(), request.unitId(), request.visitorId(), request.visitorName(), request.visitNumber());
        return ResponseEntity.ok(visit);
    }

    @GetMapping("/{areaId}/{buildingId}")
    public ResponseEntity<List<Visit>> getVisitsForBuilding(@PathVariable String areaId, @PathVariable String buildingId) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(visitService.getVisitsForBuilding(areaId, buildingId));
    }

    @GetMapping("/unit/{buildingId}/{unitId}/{visitNumber}")
    public ResponseEntity<Visit> getVisitForUnit(@PathVariable String buildingId, @PathVariable String unitId, @PathVariable int visitNumber) throws ExecutionException, InterruptedException {
        return visitService.getVisitForUnit(buildingId, unitId, visitNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public record VisitRequest(String areaId, String buildingId, String unitId, String visitorId, String visitorName, int visitNumber) {}
}
