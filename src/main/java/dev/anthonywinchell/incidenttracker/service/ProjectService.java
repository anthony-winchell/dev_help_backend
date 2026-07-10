package dev.anthonywinchell.incidenttracker.service;

import dev.anthonywinchell.incidenttracker.dto.CreateProjectRequest;
import dev.anthonywinchell.incidenttracker.dto.ProjectResponse;
import dev.anthonywinchell.incidenttracker.entity.Project;
import dev.anthonywinchell.incidenttracker.entity.User;
import dev.anthonywinchell.incidenttracker.repository.ProjectRepository;
import dev.anthonywinchell.incidenttracker.security.WorkItemPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CurrentUserService currentUserService;
    private final WorkItemPolicy workItemPolicy;

    public ProjectService(
            ProjectRepository projectRepository,
            CurrentUserService currentUserService, WorkItemPolicy workItemPolicy) {
        this.projectRepository = projectRepository;
        this.currentUserService = currentUserService;
        this.workItemPolicy = workItemPolicy;
    }

    private User getCurrentUser() {
        return currentUserService.getCurrentUser();
    }
    public ProjectResponse createProject(CreateProjectRequest request) {
        User maintainer = getCurrentUser();

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setRepoUrl(request.getRepoUrl());
        project.setMaintainer(maintainer);
        Project saved = projectRepository.save(project);
        return toResponse(saved);
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public List<ProjectResponse> findAll() {
        return projectRepository.findAll().stream().map(this::toResponse).toList();
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getRepoUrl(),
                project.getMaintainer().getUsername()
        );
    }

    public List<ProjectResponse> getProjectsForCurrentUser() {
        User currentUser = getCurrentUser();
        return projectRepository.findAllByMaintainerId(currentUser.getId()).stream().map(this::toResponse).toList();
    }

    public ProjectResponse deleteProject(Long projectId) {
        User currentUser = getCurrentUser();

        Project currentProj = getProjectById(projectId);
        if (currentProj == null) {
            throw new RuntimeException("Project not found");
        }
        workItemPolicy.canDeleteProject(currentUser, currentProj);
        projectRepository.deleteById(projectId);
        return toResponse(currentProj);

    }
}
