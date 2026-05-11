package dev.anthonywinchell.incidenttracker.entity;

import dev.anthonywinchell.incidenttracker.entity.User;
import dev.anthonywinchell.incidenttracker.entity.WorkItem;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 3, max = 100)
    private String name;

    @Size(max =255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maintainer_id", nullable = false)
    private User maintainer;

    @OneToMany(mappedBy = "project", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<WorkItem> workItems;

    @Column(nullable = false)
    @Pattern(regexp = "^https://github\\.com/[A-Za-z0-9_.-]+/[A-Za-z0-9_.-]+(\\.git)?$")
    private String repoUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
