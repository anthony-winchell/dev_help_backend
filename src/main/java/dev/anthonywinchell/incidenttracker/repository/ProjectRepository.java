package dev.anthonywinchell.incidenttracker.repository;

import dev.anthonywinchell.incidenttracker.entity.WorkItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.anthonywinchell.incidenttracker.entity.Project;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByMaintainerId(Long maintainerId);
    List<Project> findAll();
}
