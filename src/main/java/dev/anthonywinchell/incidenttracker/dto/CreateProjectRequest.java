package dev.anthonywinchell.incidenttracker.dto;

import lombok.Getter;

@Getter
public class CreateProjectRequest {
    private String name;
    private String description;
    private String repoUrl;
}
