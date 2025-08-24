package net.service.fieldmanager.servicegroup;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ServiceGroupService {

    private final ServiceGroupRepository serviceGroupRepository;

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
}
