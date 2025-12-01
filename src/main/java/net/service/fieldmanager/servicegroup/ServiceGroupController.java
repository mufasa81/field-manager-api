package net.service.fieldmanager.servicegroup;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-groups")
public class ServiceGroupController {

    private final ServiceGroupService serviceGroupService;
    private static final Logger logger = LoggerFactory.getLogger(ServiceGroupController.class);

    @PostMapping
    public ResponseEntity<?> saveServiceGroup(@RequestBody ServiceGroupRequest request) {
        try {
            ServiceGroup savedGroup = serviceGroupService.saveServiceGroup(request);
            return new ResponseEntity<>(savedGroup, HttpStatus.OK); // Or CREATED for new
        } catch (ExecutionException | InterruptedException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "저장 중 오류가 발생했습니다.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<ServiceGroup>> getServiceGroupsByDate(@RequestParam String date) {
        logger.info("Fetching service groups for date: {}", date);
        try {
            List<ServiceGroup> groups = serviceGroupService.getServiceGroupsByDate(date);
            logger.info("Found {} groups for date: {}", groups.size(), date);
            return new ResponseEntity<>(groups, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error fetching service groups for date: {}", date, e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteServiceGroup(@PathVariable String id) {
        try {
            serviceGroupService.deleteServiceGroup(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "삭제되었습니다.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "삭제 중 오류가 발생했습니다.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/my-assignments")
    public ResponseEntity<List<MyAssignedAreaDto>> getMyAssignedAreas(@RequestParam String userName, @RequestParam String serviceDate) {
        try {
            List<MyAssignedAreaDto> assignments = serviceGroupService.getAssignedAreasForUser(userName, serviceDate);
            return new ResponseEntity<>(assignments, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
