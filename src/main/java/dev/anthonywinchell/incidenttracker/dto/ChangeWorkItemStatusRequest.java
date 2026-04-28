package dev.anthonywinchell.incidenttracker.dto;

import dev.anthonywinchell.incidenttracker.enums.WorkItemStatus;

public class ChangeWorkItemStatusRequest {
    public WorkItemStatus newStatus;
    public Long actorId;
}
