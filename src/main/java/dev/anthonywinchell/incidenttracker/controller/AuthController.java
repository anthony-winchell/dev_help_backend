package dev.anthonywinchell.incidenttracker.controller;

import dev.anthonywinchell.incidenttracker.dto.LoginRequest;
import dev.anthonywinchell.incidenttracker.dto.RegisterRequest;
import dev.anthonywinchell.incidenttracker.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import dev.anthonywinchell.incidenttracker.repository.UserRepository;
import dev.anthonywinchell.incidenttracker.entity.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.username.toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(request.password));

        return userRepository.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.username.toLowerCase().trim()).orElse(null);

        if (user == null) {
            return "User not found";
        }

        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            return "Invalid password";
        }

        return jwtService.generateToken(user);
    }

    @GetMapping("/me")
    public User me(Authentication authentication) {
        if(authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new RuntimeException("Not authenticated");
        }
        return (User) authentication.getPrincipal();

    }


}
