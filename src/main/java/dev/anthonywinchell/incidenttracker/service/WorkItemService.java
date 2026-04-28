package dev.anthonywinchell.incidenttracker.service;

import dev.anthonywinchell.incidenttracker.dto.CreateWorkItemRequest;
import dev.anthonywinchell.incidenttracker.entity.WorkItem;
import dev.anthonywinchell.incidenttracker.entity.WorkItemEvent;
import dev.anthonywinchell.incidenttracker.entity.User;
import dev.anthonywinchell.incidenttracker.enums.WorkItemStatus;
import dev.anthonywinchell.incidenttracker.repository.WorkItemEventRepository;
import dev.anthonywinchell.incidenttracker.repository.WorkItemRepository;
import dev.anthonywinchell.incidenttracker.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkItemService {

    private final WorkItemRepository workItemRepository;
    private final UserRepository userRepository;
    private final WorkItemEventRepository workItemEventRepository;

    public WorkItemService(WorkItemRepository workItemRepository,
                           UserRepository userRepository,
                           WorkItemEventRepository workItemEventRepository) {
        this.workItemRepository = workItemRepository;
        this.userRepository = userRepository;
        this.workItemEventRepository = workItemEventRepository;
    }


    public WorkItem createWorkItem(CreateWorkItemRequest request) {
        User reporter = userRepository.findById(request.reporterId)
                .orElseThrow(() -> new RuntimeException("Reporter not found"));
        WorkItem workItem = new WorkItem();
        workItem.setTitle(request.title);
        workItem.setDescription(request.description);
        workItem.setPriority(request.priority);
        workItem.setType(request.type);
        workItem.setReporter(reporter);
        workItem.setStatus(WorkItemStatus.OPEN);

        return workItemRepository.save(workItem);
    }

    public WorkItem changeStatus(Long incidentId, WorkItemStatus newStatus, Long actorId) {
        WorkItem workItem = workItemRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("WorkItem not found"));

        WorkItemStatus currentStatus = workItem.getStatus();

        if (!currentStatus.allowedTransitions().contains(newStatus)) {
            throw new RuntimeException("Invalid transition: " + currentStatus + " → " + newStatus);
        }

        User actor = userRepository.findById(actorId)
                .orElseThrow(() -> new RuntimeException("Actor not found"));

        workItem.setStatus(newStatus);
        WorkItem saved = workItemRepository.save(workItem);

        WorkItemEvent event = new WorkItemEvent();
        event.setWorkItem(saved);
        event.setFromStatus(currentStatus);
        event.setToStatus(newStatus);
        event.setActor(actor);
        event.setMessage("Status changed from " + currentStatus + " to " + newStatus);
        workItemEventRepository.save(event);

        return saved;
    }


}
