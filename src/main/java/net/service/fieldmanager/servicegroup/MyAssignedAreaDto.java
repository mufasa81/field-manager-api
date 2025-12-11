package net.service.fieldmanager.servicegroup;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class MyAssignedAreaDto {
    private String id;
    private String name;
    private String assignedAt;
    private String leaderId;
    private String leaderName;
    private boolean hasDoNotVisit;
    private String lastVisited;
    private boolean isCompleted;

    private List<Member> assignedPeople;
}
