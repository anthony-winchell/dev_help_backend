package dev.anthonywinchell.incidenttracker.dto;

import dev.anthonywinchell.incidenttracker.entity.User;

public class UserResponse {
    public Long id;
    public String username;
    public String email;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }}
