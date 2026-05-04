package dev.anthonywinchell.incidenttracker.dto;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        String repoUrl,
        String maintainerUsername
) {
}
