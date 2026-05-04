package dev.anthonywinchell.incidenttracker.controller;

import dev.anthonywinchell.incidenttracker.dto.CreateProjectRequest;
import dev.anthonywinchell.incidenttracker.dto.CreateWorkItemRequest;
import dev.anthonywinchell.incidenttracker.dto.ProjectResponse;
import dev.anthonywinchell.incidenttracker.dto.WorkItemResponse;
import dev.anthonywinchell.incidenttracker.entity.Project;
import dev.anthonywinchell.incidenttracker.entity.WorkItem;
import dev.anthonywinchell.incidenttracker.service.ProjectService;
import dev.anthonywinchell.incidenttracker.service.WorkItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final WorkItemService workItemService;

    public ProjectController(ProjectService projectService,
                             WorkItemService workItemService) {
        this.projectService = projectService;
        this.workItemService = workItemService;
    }

    @GetMapping
    public Page<ProjectResponse> getAllProjects(Pageable pageable) {
        return projectService.findAll(pageable);
    }

    @GetMapping("/{projectId}")
    public ProjectResponse getProjectById(@PathVariable Long projectId) {
        return projectService.getProjectById(projectId);
    }

    @GetMapping("/maintainer/{maintainerId}")
    public Page<ProjectResponse> getProjectsByMaintainerId(
            @PathVariable Long maintainerId,
            Pageable pageable
    ) {
        return projectService.getProjectsByMaintainerId(maintainerId, pageable);
    }

    @PostMapping()
    public ProjectResponse createProject(@RequestBody CreateProjectRequest request){
        return projectService.createProject(request);
    }

    @PostMapping("/{projectId}/work-items")
    public WorkItemResponse createWorkItem(@PathVariable Long projectId,
                                   @RequestBody CreateWorkItemRequest request){
        return workItemService.createWorkItem(projectId, request);
    }

    @GetMapping("/{projectId}/work-items")
    public Page<WorkItemResponse> getWorkItemsByProjectId(@PathVariable Long projectId,
                                                          Pageable pageable){
        return workItemService.getWorkItemsByProjectId(projectId, pageable);
    }

}
