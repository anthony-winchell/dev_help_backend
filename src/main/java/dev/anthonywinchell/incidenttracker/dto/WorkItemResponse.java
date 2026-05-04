package dev.anthonywinchell.incidenttracker.dto;

import dev.anthonywinchell.incidenttracker.enums.WorkItemPriority;
import dev.anthonywinchell.incidenttracker.enums.WorkItemStatus;
import dev.anthonywinchell.incidenttracker.enums.WorkItemType;

public record WorkItemResponse(
        Long id,
        String title,
        String description,
        WorkItemPriority priority,
        WorkItemType type,
        WorkItemStatus status,
        String reporterUsername,
        String assigneeUsername,
        Long projectId
) {
}
