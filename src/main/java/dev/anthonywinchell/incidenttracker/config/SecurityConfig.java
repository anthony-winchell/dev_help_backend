package dev.anthonywinchell.incidenttracker.config;

import dev.anthonywinchell.incidenttracker.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // disable CSRF for stateless API
                .csrf(csrf -> csrf.disable())

                // IMPORTANT: use ONE CORS source
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // stateless JWT API
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // authorization rules
                .authorizeHttpRequests(auth -> auth
                        // PUBLIC READ
                        .requestMatchers(HttpMethod.GET, "/api/projects/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/work-items/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()


                        // AUTH ROUTES
                        .requestMatchers("/api/auth/**").permitAll()

                        // PROTECTED WRITE ACTIONS
                        .requestMatchers(HttpMethod.POST, "/api/projects/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/work-items/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/projects/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/projects/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/work-items/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/work-items/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/work-items/**").authenticated()

                        // fallback
                        .anyRequest().authenticated()
                )

                // JWT filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}