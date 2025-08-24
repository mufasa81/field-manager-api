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
}
