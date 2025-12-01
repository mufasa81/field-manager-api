package net.service.fieldmanager.volunteer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applicants")
public class ApplicantController {

    private final VolunteerService volunteerService;

    @GetMapping
    public ResponseEntity<List<Applicant>> getApplicantsByDate(@RequestParam String date) {
        try {
            List<Volunteer> volunteers = volunteerService.getApplicantsByDate(date);
            List<Applicant> applicants = volunteers.stream()
                    .map(v -> new Applicant(v.getId(), v.getUserName(), v.getServiceDate(), v.getServiceType()))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(applicants, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Volunteer> applyForService(@RequestBody ApplyRequest request) {
        try {
            Volunteer volunteer = volunteerService.applyForService(request);
            return new ResponseEntity<>(volunteer, HttpStatus.CREATED);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelService(@RequestBody ApplyRequest request) {
        try {
            volunteerService.cancelService(request);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
