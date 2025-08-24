package net.service.fieldmanager.areagroup;

import lombok.Getter;
import lombok.Setter;
import net.service.fieldmanager.areagroup.map.MapCoordinates;

import java.time.Instant;

@Getter
@Setter
public class AreaGroupCreationRequest {
    public AreaGroupCreationRequest() { /* No-arg constructor for Firestore */ }
    
    private String name;
    private int totalAreaCards;
    private int completedCards;
    private int totalHouseholds;
    private String description;
    private String mapCoordinates;
}
