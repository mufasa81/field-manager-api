package net.service.fieldmanager.visit;

import com.google.cloud.firestore.annotation.DocumentId;
import lombok.Data;

import java.util.Date;

@Data
public class Visit {
    @DocumentId
    private String visitId;
    private String areaId;
    private String buildingId;
    private String unitId;
    private int visitNumber;
    private Date visitDate;
    private String visitorId;
    private String visitorName;
}
