package net.service.fieldmanager.volunteer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    public Volunteer createVolunteer(VolunteerRequest request) throws ExecutionException, InterruptedException {
        Volunteer volunteer = Volunteer.builder()
                .userName(request.getUserName())
                .serviceDate(request.getServiceDate())
                .serviceType(request.getServiceType())
                .build();
        return volunteerRepository.save(volunteer);
    }

    public List<Volunteer> getAllVolunteers() throws ExecutionException, InterruptedException {
        return volunteerRepository.findAll();
    }

    public Volunteer getVolunteerById(String id) throws ExecutionException, InterruptedException {
        return volunteerRepository.findById(id).orElse(null);
    }

    public Volunteer updateVolunteer(String id, VolunteerRequest request) throws ExecutionException, InterruptedException {
        Volunteer volunteer = Volunteer.builder()
                .id(id)
                .userName(request.getUserName())
                .serviceDate(request.getServiceDate())
                .serviceType(request.getServiceType())
                .build();
        return volunteerRepository.save(volunteer);
    }

    public void deleteVolunteer(String id) throws ExecutionException, InterruptedException {
        volunteerRepository.deleteById(id);
    }

    public List<Volunteer> getApplicantsByDate(String date) throws ExecutionException, InterruptedException {
        return volunteerRepository.findAllByServiceDate(date);
    }

    public Volunteer applyForService(ApplyRequest request) throws ExecutionException, InterruptedException {
        // First, remove any other applications by this user for the same day
        List<Volunteer> existingApplications = volunteerRepository.findAllByServiceDateAndUserName(request.getServiceDate(), request.getUserName());
        for (Volunteer existing : existingApplications) {
            volunteerRepository.deleteById(existing.getId());
        }

        // Create and save the new application
        Volunteer volunteer = Volunteer.builder()
                .userId(request.getUserId())
                .userName(request.getUserName())
                .serviceDate(request.getServiceDate())
                .serviceType(request.getServiceType())
                .build();
        return volunteerRepository.save(volunteer);
    }

    public void cancelService(ApplyRequest request) throws ExecutionException, InterruptedException {
        List<Volunteer> existingApplications = volunteerRepository.findAllByServiceDateAndUserNameAndServiceType(request.getServiceDate(), request.getUserName(), request.getServiceType());
        for (Volunteer existing : existingApplications) {
            volunteerRepository.deleteById(existing.getId());
        }
    }
}
