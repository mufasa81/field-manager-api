package net.service.fieldmanager.servicegroup;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-groups")
public class ServiceGroupController {

    private final ServiceGroupService serviceGroupService;

    @PostMapping
    public ResponseEntity<ServiceGroup> saveServiceGroup(@RequestBody ServiceGroupRequest request) {
        try {
            ServiceGroup savedGroup = serviceGroupService.saveServiceGroup(request);
            return new ResponseEntity<>(savedGroup, HttpStatus.OK); // Or CREATED for new
        } catch (ExecutionException | InterruptedException e) {
            // Consider more specific error handling
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<ServiceGroup>> getServiceGroupsByDate(@RequestParam String date) {
        try {
            List<ServiceGroup> groups = serviceGroupService.getServiceGroupsByDate(date);
            return new ResponseEntity<>(groups, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteServiceGroup(@PathVariable String id) {
        try {
            serviceGroupService.deleteServiceGroup(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
