package dev.anthonywinchell.incidenttracker.enums;


import java.util.Set;

public enum IncidentStatus {
    OPEN {
        public Set<IncidentStatus> allowedTransitions() {
           return Set.of(ACKNOWLEDGED, CLOSED);
        }
    },
    ACKNOWLEDGED {
        public Set<IncidentStatus> allowedTransitions() {
            return Set.of(IN_PROGRESS, CLOSED);
        }
    },
    IN_PROGRESS {
        public Set<IncidentStatus> allowedTransitions() {
            return Set.of(RESOLVED);
        }
    },
    RESOLVED {
        public Set<IncidentStatus> allowedTransitions() {
            return Set.of(CLOSED);
        }
    },
    CLOSED {
        public Set<IncidentStatus> allowedTransitions() {
            return Set.of();
        }
    };
    public abstract Set<IncidentStatus> allowedTransitions();
}

