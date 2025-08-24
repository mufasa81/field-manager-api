package net.service.fieldmanager.building;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unit {
    private String id; // Firestore document ID for the unit
    private String value; // e.g., "101호", "GS25"
    private String memo; // New field for memo
    @JsonProperty("isDoNotVisit")
    private boolean isDoNotVisit; // New field for do not visit status
}
