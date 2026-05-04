package dev.anthonywinchell.incidenttracker.service;

import dev.anthonywinchell.incidenttracker.dto.ChangeWorkItemStatusRequest;
import dev.anthonywinchell.incidenttracker.dto.CreateWorkItemRequest;
import dev.anthonywinchell.incidenttracker.dto.WorkItemResponse;
import dev.anthonywinchell.incidenttracker.entity.Project;
import dev.anthonywinchell.incidenttracker.entity.WorkItem;
import dev.anthonywinchell.incidenttracker.entity.WorkItemEvent;
import dev.anthonywinchell.incidenttracker.entity.User;
import dev.anthonywinchell.incidenttracker.enums.WorkItemStatus;
import dev.anthonywinchell.incidenttracker.repository.ProjectRepository;
import dev.anthonywinchell.incidenttracker.repository.WorkItemEventRepository;
import dev.anthonywinchell.incidenttracker.repository.WorkItemRepository;
import dev.anthonywinchell.incidenttracker.repository.UserRepository;
import dev.anthonywinchell.incidenttracker.security.WorkItemPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class WorkItemService {

    private final WorkItemRepository workItemRepository;
    private final UserRepository userRepository;
    private final WorkItemEventRepository workItemEventRepository;
    private final ProjectRepository projectRepository;
    private final WorkItemPolicy workItemPolicy;

    public WorkItemService(WorkItemRepository workItemRepository,
                           UserRepository userRepository,
                           WorkItemEventRepository workItemEventRepository,
                           ProjectRepository projectRepository, WorkItemPolicy workItemPolicy) {
        this.workItemRepository = workItemRepository;
        this.userRepository = userRepository;
        this.workItemEventRepository = workItemEventRepository;
        this.projectRepository = projectRepository;
        this.workItemPolicy = workItemPolicy;
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public Page<WorkItemResponse> findAll(Pageable pageable){
        return workItemRepository.findAll(pageable).map(this::toResponse);
    }


    public WorkItemResponse createWorkItem(Long projectId, CreateWorkItemRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User reporter = getCurrentUser();

        WorkItem workItem = new WorkItem();
        workItem.setTitle(request.getTitle());
        workItem.setDescription(request.getDescription());
        workItem.setPriority(request.getPriority());
        workItem.setType(request.getType());
        workItem.setReporter(reporter);
        workItem.setStatus(WorkItemStatus.OPEN);
        workItem.setProject(project);

        WorkItem saved = workItemRepository.save(workItem);
        return toResponse(saved);
    }

    public WorkItemResponse changeStatus(Long workItemId, ChangeWorkItemStatusRequest request) {
        WorkItem workItem = workItemRepository.findById(workItemId)
                .orElseThrow(() -> new RuntimeException("WorkItem not found"));

        User currentUser = getCurrentUser();

        WorkItemStatus currentStatus = workItem.getStatus();

        if (!currentStatus.allowedTransitions().contains(request.newStatus)){
            throw new RuntimeException("Invalid status transition");
        }

        workItemPolicy.canUpdateStatus(currentUser, workItem);

        workItem.setStatus(request.newStatus);
        WorkItem saved = workItemRepository.save(workItem);

        WorkItemEvent event = new WorkItemEvent();
        event.setWorkItem(workItem);
        event.setFromStatus(currentStatus);
        event.setToStatus(request.newStatus);
        event.setActor(currentUser);
        workItemEventRepository.save(event);

        return toResponse(saved);
    }

    public Page<WorkItemResponse> getWorkItemsByProjectId(Long projectId, Pageable pageable) {
        return workItemRepository.findByProjectId(projectId, pageable).map(this::toResponse);
    }

    public WorkItemResponse claimWorkItem(Long workItemId) {
        WorkItem workItem = workItemRepository.findById(workItemId)
                .orElseThrow(() -> new RuntimeException("WorkItem not found"));

        User currentUser = getCurrentUser();
        workItemPolicy.canClaim(currentUser, workItem);

        workItem.setAssignee(currentUser);
        workItem.setStatus(WorkItemStatus.IN_PROGRESS);

        WorkItem saved = workItemRepository.save(workItem);

        WorkItemEvent event = new WorkItemEvent();
        event.setWorkItem(workItem);
        event.setFromStatus(WorkItemStatus.OPEN);
        event.setToStatus(WorkItemStatus.IN_PROGRESS);
        event.setActor(currentUser);
        workItemEventRepository.save(event);

        return toResponse(saved);

    }

    private WorkItemResponse toResponse(WorkItem workItem) {
        return new WorkItemResponse(
                workItem.getId(),
                workItem.getTitle(),
                workItem.getDescription(),
                workItem.getPriority(),
                workItem.getType(),
                workItem.getStatus(),
                workItem.getReporter().getUsername(),
                workItem.getAssignee() != null ? workItem.getAssignee().getUsername() : null,
                workItem.getProject().getId()
        );
    }

}
