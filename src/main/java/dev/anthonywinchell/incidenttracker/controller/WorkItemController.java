package dev.anthonywinchell.incidenttracker.controller;

import dev.anthonywinchell.incidenttracker.dto.ChangeWorkItemStatusRequest;
import dev.anthonywinchell.incidenttracker.dto.CreateWorkItemRequest;
import dev.anthonywinchell.incidenttracker.dto.WorkItemResponse;
import dev.anthonywinchell.incidenttracker.entity.WorkItem;
import dev.anthonywinchell.incidenttracker.service.WorkItemService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/work-items")
public class WorkItemController {

    private final WorkItemService workItemService;

    public WorkItemController(WorkItemService workItemService) {
        this.workItemService = workItemService;
    }

    @GetMapping()
    public List<WorkItemResponse> getAllWorkItems(){
        return workItemService.findAll();
    }

    @PatchMapping("/{id}/status")
    public WorkItemResponse changeStatus(@PathVariable Long id,
                                 @RequestBody ChangeWorkItemStatusRequest request){
        return workItemService.changeStatus(id, request);
    }
    //this is temporary and will be fixed once authentication is implemented
    @PatchMapping("/{workItemId}/claim")
    public WorkItemResponse claimWorkItem(@PathVariable Long workItemId){
        return workItemService.claimWorkItem(workItemId);
    }



}
