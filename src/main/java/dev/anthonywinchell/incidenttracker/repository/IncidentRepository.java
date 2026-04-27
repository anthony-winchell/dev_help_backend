package dev.anthonywinchell.incidenttracker.repository;

import dev.anthonywinchell.incidenttracker.entity.Incident;
import dev.anthonywinchell.incidenttracker.enums.IncidentSeverity;
import dev.anthonywinchell.incidenttracker.enums.IncidentStatus;
import dev.anthonywinchell.incidenttracker.enums.IncidentType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    Page<Incident> findByStatus(IncidentStatus status, Pageable pageable);
    Page<Incident> findBySeverity(IncidentSeverity severity, Pageable pageable);
    Page<Incident> findByAssigneeId(Long assigneeId, Pageable pageable);
    Page<Incident> findByReporterId(Long reporterId, Pageable pageable);
    Page<Incident> findByType(IncidentType type, Pageable pageable);
}
