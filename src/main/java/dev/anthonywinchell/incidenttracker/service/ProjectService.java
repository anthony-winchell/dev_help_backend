package dev.anthonywinchell.incidenttracker.service;

import dev.anthonywinchell.incidenttracker.dto.CreateProjectRequest;
import dev.anthonywinchell.incidenttracker.dto.ProjectResponse;
import dev.anthonywinchell.incidenttracker.entity.Project;
import dev.anthonywinchell.incidenttracker.entity.User;
import dev.anthonywinchell.incidenttracker.entity.WorkItem;
import dev.anthonywinchell.incidenttracker.repository.ProjectRepository;
import dev.anthonywinchell.incidenttracker.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof User user)) {
            throw new RuntimeException("User not authenticated");
        }

        return user;
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

    public List<ProjectResponse> getProjectsByMaintainerId(Long maintainerId) {
        return projectRepository.findAllByMaintainerId(maintainerId).stream().map(this::toResponse).toList();
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

}
