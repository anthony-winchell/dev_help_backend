package dev.anthonywinchell.incidenttracker.service;

import dev.anthonywinchell.incidenttracker.dto.CreateIncidentRequest;
import dev.anthonywinchell.incidenttracker.entity.Incident;
import dev.anthonywinchell.incidenttracker.entity.IncidentEvent;
import dev.anthonywinchell.incidenttracker.entity.User;
import dev.anthonywinchell.incidenttracker.enums.IncidentStatus;
import dev.anthonywinchell.incidenttracker.repository.IncidentEventRepository;
import dev.anthonywinchell.incidenttracker.repository.IncidentRepository;
import dev.anthonywinchell.incidenttracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;
    private final IncidentEventRepository incidentEventRepository;

    public IncidentService(IncidentRepository incidentRepository,
                           UserRepository userRepository,
                           IncidentEventRepository incidentEventRepository) {
        this.incidentRepository = incidentRepository;
        this.userRepository = userRepository;
        this.incidentEventRepository = incidentEventRepository;
    }


    public Incident createIncident(CreateIncidentRequest request) {
        User reporter = userRepository.findById(request.reporterId)
                .orElseThrow(() -> new RuntimeException("Reporter not found"));
        Incident incident = new Incident();
        incident.setTitle(request.title);
        incident.setDescription(request.description);
        incident.setSeverity(request.severity);
        incident.setType(request.type);
        incident.setReporter(reporter);
        incident.setStatus(IncidentStatus.OPEN);

        return incidentRepository.save(incident);
    }

    public Incident changeStatus(Long incidentId, IncidentStatus newStatus, Long actorId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident not found"));

        IncidentStatus currentStatus = incident.getStatus();

        if (!currentStatus.allowedTransitions().contains(newStatus)) {
            throw new RuntimeException("Invalid transition: " + currentStatus + " → " + newStatus);
        }

        incident.setStatus(newStatus);

        Incident saved = incidentRepository.save(incident);
        User actor = userRepository.findById(actorId)
                .orElseThrow(() -> new RuntimeException("Actor not found"));

        IncidentEvent event = new IncidentEvent();
        event.setIncident(saved);
        event.setFromStatus(currentStatus);
        event.setToStatus(newStatus);
        event.setActor(actor);
        event.setMessage("Status changed");
        incidentEventRepository.save(event);

        return saved;
    }


}
