package dev.anthonywinchell.incidenttracker.repository;

import dev.anthonywinchell.incidenttracker.entity.User;
import dev.anthonywinchell.incidenttracker.entity.WorkItem;
import dev.anthonywinchell.incidenttracker.enums.WorkItemPriority;
import dev.anthonywinchell.incidenttracker.enums.WorkItemStatus;
import dev.anthonywinchell.incidenttracker.enums.WorkItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkItemRepository extends JpaRepository<WorkItem, Long> {
    Page<WorkItem> findByStatus(WorkItemStatus status, Pageable pageable);
    Page<WorkItem> findByPriority(WorkItemPriority priority, Pageable pageable);
    Page<WorkItem> findByAssigneeId(Long assigneeId, Pageable pageable);
    Page<WorkItem> findByReporterId(Long reporterId, Pageable pageable);
    Page<WorkItem> findByType(WorkItemType type, Pageable pageable);
    List<WorkItem> findByProjectId(Long projectId);
    List<WorkItem> findAll();
    List<WorkItem> findByAssignee(User assignee);
}
