package dev.anthonywinchell.incidenttracker.enums;


import java.util.Set;

public enum WorkItemStatus {
    OPEN {
        public Set<WorkItemStatus> allowedTransitions() {
           return Set.of(IN_PROGRESS, CLOSED);
        }
    },
    IN_PROGRESS {
        public Set<WorkItemStatus> allowedTransitions() {
            return Set.of(RESOLVED);
        }
    },
    RESOLVED {
        public Set<WorkItemStatus> allowedTransitions() {
            return Set.of(CLOSED);
        }
    },
    CLOSED {
        public Set<WorkItemStatus> allowedTransitions() {
            return Set.of();
        }
    };
    public abstract Set<WorkItemStatus> allowedTransitions();
}

