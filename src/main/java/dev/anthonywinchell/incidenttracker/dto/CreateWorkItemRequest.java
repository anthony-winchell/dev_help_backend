package dev.anthonywinchell.incidenttracker.dto;

import dev.anthonywinchell.incidenttracker.enums.WorkItemPriority;
import dev.anthonywinchell.incidenttracker.enums.WorkItemType;

public class CreateWorkItemRequest {
    public String title;
    public String description;
    public WorkItemPriority priority;
    public WorkItemType type;
    public Long reporterId;
}