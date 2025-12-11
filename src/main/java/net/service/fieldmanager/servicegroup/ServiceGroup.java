package net.service.fieldmanager.servicegroup;

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
public class ServiceGroup {
    @DocumentId
    private String id;
    private String groupName;
    private String serviceDate; // YYYY-MM-DD
    private String leaderId;
    private List<String> assignedAreaIds;
    private List<String> assignedAreaNames;
    private List<Member> members;
    @ServerTimestamp
    private Instant createdDate;
    @ServerTimestamp
    private Instant lastModifiedDate;
}
