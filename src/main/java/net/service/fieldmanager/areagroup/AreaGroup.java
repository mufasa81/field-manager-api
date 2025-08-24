package net.service.fieldmanager.areagroup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
public class AreaGroup {
    public AreaGroup() { /* No-arg constructor for Firestore */ }

    @DocumentId
    private String id;
    private String name;
    private int totalAreaCards;
    private int completedCards;
    private int totalHouseholds;
    private String description;
    private String mapCoordinates; // Store as JSON string
    private int areaCount; // 실제 등록된 구역카드 수

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void setMapCoordinates(Object mapCoordinates) {
        if (mapCoordinates instanceof String) {
            this.mapCoordinates = (String) mapCoordinates;
        } else if (mapCoordinates instanceof Map) {
            try {
                this.mapCoordinates = objectMapper.writeValueAsString(mapCoordinates);
            } catch (JsonProcessingException e) {
                // Handle exception, perhaps log it
                this.mapCoordinates = null;
            }
        }
    }
    
}
