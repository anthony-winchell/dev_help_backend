package dev.anthonywinchell.incidenttracker.dto;

import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @Size(min = 8, max = 20, message = "Username must be between 8 and 20 characters")
    public String username;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    public String password;
}
