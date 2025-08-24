package net.service.fieldmanager.areagroup;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/area-groups")
public class AreaGroupController {

    private final AreaGroupService areaGroupService;

    @PostMapping
    public ResponseEntity<AreaGroup> createAreaGroup(@RequestBody AreaGroupCreationRequest request) throws ExecutionException, InterruptedException {
        AreaGroup newAreaGroup = areaGroupService.createAreaGroup(request);
        return ResponseEntity.ok(newAreaGroup);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaGroup> getAreaGroupById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return areaGroupService.getAreaGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AreaGroup>> getAllAreaGroups() throws ExecutionException, InterruptedException {
        List<AreaGroup> areaGroups = areaGroupService.getAllAreaGroups();
        return ResponseEntity.ok(areaGroups);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaGroup> updateAreaGroup(@PathVariable String id, @RequestBody AreaGroupUpdateRequest request) throws ExecutionException, InterruptedException {
        AreaGroup updatedAreaGroup = areaGroupService.updateAreaGroup(id, request);
        return ResponseEntity.ok(updatedAreaGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAreaGroup(@PathVariable String id) {
        areaGroupService.deleteAreaGroup(id);
        return ResponseEntity.noContent().build();
    }
}
