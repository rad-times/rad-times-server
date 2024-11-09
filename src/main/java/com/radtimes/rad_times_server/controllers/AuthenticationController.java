package com.radtimes.rad_times_server.controllers;

import com.radtimes.rad_times_server.model.PersonModel;
import com.radtimes.rad_times_server.service.GoogleAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class AuthenticationController {
    private final GoogleAuthenticationService googleAuthenticationService;

    public AuthenticationController(GoogleAuthenticationService googleAuthenticationService) {
        this.googleAuthenticationService = googleAuthenticationService;
    }

    @GetMapping("/authenticate")
    public PersonModel authenticate(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            if (authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring("Bearer ".length());
                return googleAuthenticationService.validateGoogleAuthToken(token);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
            }
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
    }
}
