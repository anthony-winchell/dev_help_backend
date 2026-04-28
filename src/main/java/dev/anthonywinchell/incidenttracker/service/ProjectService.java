package dev.anthonywinchell.incidenttracker.service;

import dev.anthonywinchell.incidenttracker.entity.Project;
import dev.anthonywinchell.incidenttracker.entity.User;
import dev.anthonywinchell.incidenttracker.repository.ProjectRepository;
import dev.anthonywinchell.incidenttracker.repository.UserRepository;
import dev.anthonywinchell.incidenttracker.repository.WorkItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            UserRepository userRepository){
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project createProject(Project project, Long maintainerId){
        User maintainer = userRepository.findById(maintainerId)
                .orElseThrow(() -> new RuntimeException("Maintainer not found"));

        project.setMaintainer(maintainer);
        return projectRepository.save(project);
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public Page<Project> getProjectsByMaintainerId(Long maintainerId, Pageable pageable) {
        return projectRepository.findAllByMaintainerId(maintainerId, pageable);
    }



}
