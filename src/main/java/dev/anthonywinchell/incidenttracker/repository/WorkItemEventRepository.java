package dev.anthonywinchell.incidenttracker.repository;

import dev.anthonywinchell.incidenttracker.entity.WorkItemEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkItemEventRepository extends JpaRepository<WorkItemEvent, Long> {
    List<WorkItemEvent> findByWorkItemId(Long workItemId);
}
