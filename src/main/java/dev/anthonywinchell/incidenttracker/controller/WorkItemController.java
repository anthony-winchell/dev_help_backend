package dev.anthonywinchell.incidenttracker.controller;

import dev.anthonywinchell.incidenttracker.dto.ChangeWorkItemStatusRequest;
import dev.anthonywinchell.incidenttracker.dto.CreateWorkItemRequest;
import dev.anthonywinchell.incidenttracker.entity.WorkItem;
import dev.anthonywinchell.incidenttracker.service.WorkItemService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/work-items")
public class WorkItemController {

    private final WorkItemService workItemService;

    public WorkItemController(WorkItemService workItemService) {
        this.workItemService = workItemService;
    }

    @GetMapping()
    public Page<WorkItem> getAllWorkItems(Pageable pageable){
        return workItemService.findAll(pageable);
    }

    @PatchMapping("/{id}/status")
    public WorkItem changeStatus(@PathVariable Long id,
                                 @RequestBody ChangeWorkItemStatusRequest request){
        return workItemService.changeStatus(id, request);
    }
    //this is temporary and will be fixed once authentication is implemented
    @PatchMapping("/{workItemId}/claim/{userId}")
    public WorkItem claimWorkItem(@PathVariable Long workItemId, @PathVariable Long userId){
        return workItemService.claimWorkItem(workItemId, userId);
    }


}
