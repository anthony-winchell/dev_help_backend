package dev.anthonywinchell.incidenttracker.controller;

import dev.anthonywinchell.incidenttracker.dto.ProjectResponse;
import dev.anthonywinchell.incidenttracker.dto.UserResponse;
import dev.anthonywinchell.incidenttracker.dto.WorkItemResponse;
import dev.anthonywinchell.incidenttracker.entity.User;
import dev.anthonywinchell.incidenttracker.repository.UserRepository;
import dev.anthonywinchell.incidenttracker.service.ProjectService;
import dev.anthonywinchell.incidenttracker.service.WorkItemService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/me")
public class MeController {

    private final ProjectService projectService;
    private final WorkItemService workItemService;
    private final UserRepository userRepository;


    public MeController(ProjectService projectService, WorkItemService workItemService, UserRepository userRepository) {
        this.projectService = projectService;
        this.workItemService = workItemService;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public UserResponse me(Authentication authentication){
        String username = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow();

        return new UserResponse(user.getId(), user.getUsername());
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
