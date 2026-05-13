package dev.anthonywinchell.incidenttracker.dto;

import dev.anthonywinchell.incidenttracker.entity.Project;
import dev.anthonywinchell.incidenttracker.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjResp {

    private Long id;
    private String name;
    private String description;
    private String repoUrl;
    private String maintainerUsername;
    private boolean isOwner;

    public ProjResp(Project project, String username) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.repoUrl = project.getRepoUrl();
        this.maintainerUsername = project.getMaintainer().getUsername();
        this.isOwner = username != null &&
                username.equals(project.getMaintainer().getUsername());
    }
}
