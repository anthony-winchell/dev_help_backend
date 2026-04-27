package dev.anthonywinchell.incidenttracker.dto;

import dev.anthonywinchell.incidenttracker.enums.IncidentSeverity;
import dev.anthonywinchell.incidenttracker.enums.IncidentType;

public class CreateIncidentRequest {
    public String title;
    public String description;
    public IncidentSeverity severity;
    public IncidentType type;
    public Long reporterId;
}