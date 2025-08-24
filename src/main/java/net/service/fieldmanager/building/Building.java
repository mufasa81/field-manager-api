package net.service.fieldmanager.building;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.ServerTimestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Building {
    @DocumentId
    private String id;
    private String areaId; // Foreign key to Area
    private BuildingType buildingType;
    private String roadAddress;
    private String apartmentName; // For APARTMENT type
    private String commercialName; // For COMMERCIAL type
    private List<Unit> units; // List of units (ho-su or commercial names)
    @ServerTimestamp
    private Instant createdDate;
    @ServerTimestamp
    private Instant lastModifiedDate;
}
