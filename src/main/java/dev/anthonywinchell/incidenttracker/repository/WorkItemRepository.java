package dev.anthonywinchell.incidenttracker.repository;

import dev.anthonywinchell.incidenttracker.entity.WorkItem;
import dev.anthonywinchell.incidenttracker.enums.WorkItemPriority;
import dev.anthonywinchell.incidenttracker.enums.WorkItemStatus;
import dev.anthonywinchell.incidenttracker.enums.WorkItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface WorkItemRepository extends JpaRepository<WorkItem, Long> {
    Page<WorkItem> findByStatus(WorkItemStatus status, Pageable pageable);
    Page<WorkItem> findByPriority(WorkItemPriority priority, Pageable pageable);
    Page<WorkItem> findByAssigneeId(Long assigneeId, Pageable pageable);
    Page<WorkItem> findByReporterId(Long reporterId, Pageable pageable);
    Page<WorkItem> findByType(WorkItemType type, Pageable pageable);
}
