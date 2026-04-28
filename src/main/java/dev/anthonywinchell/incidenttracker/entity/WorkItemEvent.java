package dev.anthonywinchell.incidenttracker.entity;

import dev.anthonywinchell.incidenttracker.enums.WorkItemStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "work_item_events")
public class WorkItemEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false)
    private WorkItem workItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkItemStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkItemStatus toStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User actor;

    @Column(nullable = false)
    private String message;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
