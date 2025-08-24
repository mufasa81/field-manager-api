package net.service.fieldmanager.areagroup;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AreaGroupUpdateRequest {
    private String name;
    private Integer totalAreaCards;
    private Integer completedCards;
    private Integer totalHouseholds;
    private String description;
    private String mapCoordinates;
}