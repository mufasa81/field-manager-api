package net.service.fieldmanager.volunteer;

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
public class Volunteer {
    @DocumentId
    private String id;
    private String userId;
    private String userName;
    private String serviceDate; // YYYY-MM-DD
    private ServiceType serviceType;
    @ServerTimestamp
    private Instant createdDate;
    @ServerTimestamp
    private Instant lastModifiedDate;
}
