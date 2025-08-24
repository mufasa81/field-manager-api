package net.service.fieldmanager.volunteer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VolunteerRequest {
    private String userName;
    private String serviceDate;
    private ServiceType serviceType;
}
