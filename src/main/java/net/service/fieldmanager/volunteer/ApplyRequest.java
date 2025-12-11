package net.service.fieldmanager.volunteer;

import lombok.Data;

@Data
public class ApplyRequest {
    private String serviceDate; // YYYY-MM-DD
    private ServiceType serviceType;
    private String userId;
    private String userName; // TODO: Remove this when security is implemented
}
