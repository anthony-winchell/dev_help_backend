package dev.anthonywinchell.incidenttracker.controller;

import dev.anthonywinchell.incidenttracker.dto.ProjectResponse;
import dev.anthonywinchell.incidenttracker.dto.WorkItemResponse;
import dev.anthonywinchell.incidenttracker.service.ProjectService;
import dev.anthonywinchell.incidenttracker.service.WorkItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/me")
public class MeController {

    private final ProjectService projectService;
    private final WorkItemService workItemService;

    public MeController(ProjectService projectService, WorkItemService workItemService) {
        this.projectService = projectService;
        this.workItemService = workItemService;
    }

    @GetMapping("/projects")
    public List<ProjectResponse> getMyProjects() {
        return projectService.getProjectsForCurrentUser();
    }

    @GetMapping("/assignments")
    public List<WorkItemResponse> getMyAssignments() {
        return workItemService.getAssignmentsForCurrentUser();
    }
}
