package net.service.fieldmanager.servicegroup;

import lombok.RequiredArgsConstructor;
import net.service.fieldmanager.area.AreaService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceGroupService {

    private final ServiceGroupRepository serviceGroupRepository;
    private final AreaService areaService; // Inject AreaService

    public ServiceGroup saveServiceGroup(ServiceGroupRequest request) throws ExecutionException, InterruptedException {
        ServiceGroup serviceGroup = ServiceGroup.builder()
                .id(request.getId()) // Pass id for updates
                .groupName(request.getGroupName())
                .serviceDate(request.getServiceDate())
                .assignedAreaIds(request.getAssignedAreaIds())
                .assignedAreaNames(request.getAssignedAreaNames())
                .members(request.getMembers())
                .build();
        return serviceGroupRepository.save(serviceGroup);
    }

    public List<ServiceGroup> getServiceGroupsByDate(String serviceDate) throws ExecutionException, InterruptedException {
        return serviceGroupRepository.findAllByServiceDate(serviceDate);
    }

    public void deleteServiceGroup(String id) {
        serviceGroupRepository.deleteById(id);
    }

    public List<MyAssignedAreaDto> getAssignedAreasForUser(String userName, String serviceDate) throws ExecutionException, InterruptedException {
        // Fetch groups only for the specified service date
        List<ServiceGroup> groupsForDate = serviceGroupRepository.findAllByServiceDate(serviceDate);

        // Filter these groups to find the ones the user is a member of
        List<ServiceGroup> userGroups = groupsForDate.stream()
                .filter(group -> group.getMembers() != null && group.getMembers().stream()
                        .anyMatch(member -> member.getName() != null && member.getName().equals(userName)))
                .collect(Collectors.toList());

        List<MyAssignedAreaDto> assignedAreas = new ArrayList<>();
        for (ServiceGroup group : userGroups) {
            if (group.getAssignedAreaIds() != null) {
                for (String areaId : group.getAssignedAreaIds()) {
                    net.service.fieldmanager.area.Area area = areaService.getAreaById(areaId);
                    if (area != null) {
                        MyAssignedAreaDto dto = MyAssignedAreaDto.builder()
                                .id(group.getId() + "-" + area.getId()) // Create a unique composite ID
                                .name(area.getAreaName())
                                .assignedAt(group.getServiceDate()) // Using the group's service date
                                .assignedPeople(group.getMembers())
                                .hasDoNotVisit(area.isHasDoNotVisit())
                                .lastVisited(formatLastVisited(area.getLastVisited()))
                                .isCompleted(areaService.isAreaCompleted(area)) // Calculate completion status via AreaService
                                .build();
                        assignedAreas.add(dto);
                    }
                }
            }
        }
        return assignedAreas;
    }

    private String formatLastVisited(Instant lastVisited) {
        if (lastVisited == null) {
            return null;
        }
        Duration duration = Duration.between(lastVisited, Instant.now());
        long days = duration.toDays();
        if (days == 0) {
            return "오늘";
        } else if (days < 7) {
            return days + "일 전";
        } else if (days < 30) {
            return (days / 7) + "주 전";
        } else if (days < 365) {
            return (days / 30) + "달 전";
        } else {
            return (days / 365) + "년 전";
        }
    }
}
