package dev.anthonywinchell.incidenttracker.service;

import dev.anthonywinchell.incidenttracker.dto.ChangeWorkItemStatusRequest;
import dev.anthonywinchell.incidenttracker.dto.CreateWorkItemRequest;
import dev.anthonywinchell.incidenttracker.entity.Project;
import dev.anthonywinchell.incidenttracker.entity.WorkItem;
import dev.anthonywinchell.incidenttracker.entity.WorkItemEvent;
import dev.anthonywinchell.incidenttracker.entity.User;
import dev.anthonywinchell.incidenttracker.enums.WorkItemStatus;
import dev.anthonywinchell.incidenttracker.repository.ProjectRepository;
import dev.anthonywinchell.incidenttracker.repository.WorkItemEventRepository;
import dev.anthonywinchell.incidenttracker.repository.WorkItemRepository;
import dev.anthonywinchell.incidenttracker.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WorkItemService {

    private final WorkItemRepository workItemRepository;
    private final UserRepository userRepository;
    private final WorkItemEventRepository workItemEventRepository;
    private final ProjectRepository projectRepository;

    public WorkItemService(WorkItemRepository workItemRepository,
                           UserRepository userRepository,
                           WorkItemEventRepository workItemEventRepository,
                           ProjectRepository projectRepository) {
        this.workItemRepository = workItemRepository;
        this.userRepository = userRepository;
        this.workItemEventRepository = workItemEventRepository;
        this.projectRepository = projectRepository;
    }

    public Page<WorkItem> findAll(Pageable pageable){
        return workItemRepository.findAll(pageable);
    }


    public WorkItem createWorkItem(Long projectId, CreateWorkItemRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User reporter = userRepository.findById(request.reporterId)
                .orElseThrow(() -> new RuntimeException("Reporter not found"));
        WorkItem workItem = new WorkItem();
        workItem.setTitle(request.title);
        workItem.setDescription(request.description);
        workItem.setPriority(request.priority);
        workItem.setType(request.type);
        workItem.setReporter(reporter);
        workItem.setStatus(WorkItemStatus.OPEN);
        workItem.setProject(project);

        return workItemRepository.save(workItem);
    }

    public WorkItem changeStatus(Long workItemId, ChangeWorkItemStatusRequest request) {
        WorkItem workItem = workItemRepository.findById(workItemId)
                .orElseThrow(() -> new RuntimeException("WorkItem not found"));

        WorkItemStatus currentStatus = workItem.getStatus();

        if(request.newStatus == null){
            throw new RuntimeException("newStatus can not be null");
        }

        if(!currentStatus.allowedTransitions().contains(request.newStatus)){
            throw new RuntimeException("Invalid status transition");
        }

        User actor = userRepository.findById(request.actorId)
                .orElseThrow(() -> new RuntimeException("Actor not found"));

        workItem.setStatus(request.newStatus);
        WorkItem saved = workItemRepository.save(workItem);

        WorkItemEvent event = new WorkItemEvent();
        event.setWorkItem(saved);
        event.setFromStatus(currentStatus);
        event.setToStatus(request.newStatus);
        event.setActor(actor);

        workItemEventRepository.save(event);
        return saved;

    }

    public Page<WorkItem> getWorkItemsByProjectId(Long projectId, Pageable pageable) {
        return workItemRepository.findByProjectId(projectId, pageable);
    }

    public WorkItem claimWorkItem(Long workItemId, Long userId) {
        WorkItem workItem = workItemRepository.findById(workItemId)
                .orElseThrow(() -> new RuntimeException("WorkItem not found"));

        if(workItem.getAssignee() != null){
            throw new RuntimeException("Already claimed");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        WorkItemStatus currentStatus = workItem.getStatus();

        workItem.setAssignee(user);
        WorkItem saved = workItemRepository.save(workItem);

        WorkItemEvent event = new WorkItemEvent();
        event.setWorkItem(saved);
        event.setFromStatus(currentStatus);
        event.setToStatus(currentStatus);
        event.setActor(user);
        workItemEventRepository.save(event);
        return saved;
    }

}
