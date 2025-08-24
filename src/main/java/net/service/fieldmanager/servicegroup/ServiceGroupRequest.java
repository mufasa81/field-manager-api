package net.service.fieldmanager.servicegroup;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceGroupRequest {
    private String id; // Can be null for new groups
    private String groupName;
    private String serviceDate;
    private List<String> assignedAreaIds;
    private List<String> assignedAreaNames;
    private List<Member> members;
}
