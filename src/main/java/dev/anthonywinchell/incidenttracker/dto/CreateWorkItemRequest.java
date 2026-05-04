package dev.anthonywinchell.incidenttracker.dto;

import dev.anthonywinchell.incidenttracker.enums.WorkItemPriority;
import dev.anthonywinchell.incidenttracker.enums.WorkItemType;
import lombok.Getter;

@Getter
public class CreateWorkItemRequest {
    private String title;
    private String description;
    private WorkItemPriority priority;
    private WorkItemType type;
}