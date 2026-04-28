package dev.anthonywinchell.incidenttracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.anthonywinchell.incidenttracker.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByMaintainerId(Long maintainerId, Pageable pageable);
}
