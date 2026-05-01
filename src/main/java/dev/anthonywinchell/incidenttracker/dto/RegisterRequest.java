package dev.anthonywinchell.incidenttracker.dto;

import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @Size(min = 8, max = 20, message = "Username must be between 8 and 20 characters")
    public String username;
    public String email;
    public String password;
}
