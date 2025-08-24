package net.service.fieldmanager.area;

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
public class Area {
    @DocumentId
    private String id;
    private String areaName;
    private String startAddress;
    private int buildingCount;
    private int completedCount;
    private boolean isActive;
    private String mapCoordinates; // GeoJSON string
    private String areaGroupId;
    @ServerTimestamp
    private Instant createdDate;
    @ServerTimestamp
    private Instant lastModifiedDate;
}
