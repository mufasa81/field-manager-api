package net.service.fieldmanager.visit;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardVisitHistory {
    @DocumentId
    private String id;
    private String areaId;
    private String buildingId;
    private String visitType; // e.g., "ALL_ABSENT", "LOCKED", "FIRST_VISIT", "SECOND_VISIT"
    private Instant visitDate;
    
    // If the visit is for a specific unit
    private String unitId;
    private Integer visitNumber; // e.g., 1 for first visit, 2 for second
    
    // User who recorded the visit
    private String userId;
    private String userName;
}
