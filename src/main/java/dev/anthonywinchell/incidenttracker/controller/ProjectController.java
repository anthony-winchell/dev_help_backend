package dev.anthonywinchell.incidenttracker.controller;

import com.sun.tools.jconsole.JConsoleContext;
import dev.anthonywinchell.incidenttracker.dto.*;
import dev.anthonywinchell.incidenttracker.entity.Project;
import dev.anthonywinchell.incidenttracker.entity.User;
import dev.anthonywinchell.incidenttracker.entity.WorkItem;
import dev.anthonywinchell.incidenttracker.service.ProjectService;
import dev.anthonywinchell.incidenttracker.service.WorkItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public List<ProjectResponse> getAllProjects() {
        return projectService.findAll();
    }

    @GetMapping("/{projectId}")
    public ProjResp getProjectById(@PathVariable Long projectId) {
        Project project = projectService.getProjectById(projectId);

        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        String username = null; // default to null for unauthenticated users

        if (principal instanceof User user) {
            username = user.getUsername();
        }

        return new ProjResp(project, username);
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
    public List<WorkItemResponse> getWorkItemsByProjectId(@PathVariable Long projectId
                                                          ){
        return workItemService.getWorkItemsByProjectId(projectId);
    }

    @DeleteMapping("/{projectId}")
    public ProjectResponse deleteProject(@PathVariable Long projectId) {
        return projectService.deleteProject(projectId);
    }

}
