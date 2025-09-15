package net.service.fieldmanager.volunteer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                    .map(v -> new Applicant(v.getId(), v.getUserName()))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(applicants, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
