package dev.anthonywinchell.incidenttracker.entity;

import dev.anthonywinchell.incidenttracker.enums.WorkItemPriority;
import dev.anthonywinchell.incidenttracker.enums.WorkItemStatus;
import dev.anthonywinchell.incidenttracker.enums.WorkItemType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "work_items")
public class WorkItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkItemPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkItemType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkItemStatus status;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public WorkItem() {
        this.status = WorkItemStatus.OPEN;
    }

}
