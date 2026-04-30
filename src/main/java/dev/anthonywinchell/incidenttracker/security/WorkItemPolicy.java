package dev.anthonywinchell.incidenttracker.security;

import dev.anthonywinchell.incidenttracker.entity.User;
import dev.anthonywinchell.incidenttracker.entity.WorkItem;
import org.springframework.stereotype.Component;

@Component
public class WorkItemPolicy {

    public void canClaim(User user, WorkItem workItem) {
        if (workItem.getAssignee() != null) {
            throw new RuntimeException("Already claimed");
        }
    }

    public void canUpdateStatus(User user, WorkItem workItem) {
        if ((workItem.getAssignee() == null) ||
        (!workItem.getAssignee().getId().equals(user.getId()))){
            throw new RuntimeException("Only assignee can update status");
        }
    }

    public void canClose(User user, WorkItem workItem) {
        if (!workItem.getProject().getMaintainer().getId().equals(user.getId())) {
            throw new RuntimeException("Only maintainer can close");
        }
    }
}
