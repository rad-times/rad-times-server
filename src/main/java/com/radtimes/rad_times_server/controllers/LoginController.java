package com.radtimes.rad_times_server.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.radtimes.rad_times_server.model.PersonModel;
import com.radtimes.rad_times_server.service.GoogleAuthenticationService;
import com.radtimes.rad_times_server.service.PersonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
public class LoginController {
    private final GoogleAuthenticationService googleAuthenticationService;
    private final PersonService personService;

    public LoginController(GoogleAuthenticationService googleAuthenticationService, PersonService personService) {
        this.googleAuthenticationService = googleAuthenticationService;
        this.personService = personService;
    }

    @GetMapping("/login")
    public ResponseEntity<PersonModel> authenticate(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            if (authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring("Bearer ".length());
                GoogleIdToken.Payload idTokenPayload =  googleAuthenticationService.validateGoogleAuthToken(token);

                Optional<PersonModel> matchingPerson = personService.getActivePersonByAuthId(idTokenPayload.getSubject());
                PersonModel person =  matchingPerson.orElseGet(() -> personService.createPersonFromAuthData(idTokenPayload));
                return new ResponseEntity<>(person, HttpStatus.OK);

            } else {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
}
