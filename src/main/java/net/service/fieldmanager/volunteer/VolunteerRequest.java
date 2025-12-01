package net.service.fieldmanager.volunteer;

import lombok.Data;

@Data
public class VolunteerRequest {
    private String userName;
    private String serviceDate; // YYYY-MM-DD
    private ServiceType serviceType;
}