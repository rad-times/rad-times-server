package com.radtimes.rad_times_server.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.radtimes.rad_times_server.model.PersonModel;
import com.radtimes.rad_times_server.model.oauth.FacebookTokenPayload;
import com.radtimes.rad_times_server.service.oauth.FacebookAuthenticationService;
import com.radtimes.rad_times_server.service.oauth.GoogleAuthenticationService;
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
    private final FacebookAuthenticationService facebookAuthenticationService;
    private final PersonService personService;

    private enum AUTH_TYPES {
        GOOGLE("google"),
        FACEBOOK("facebook");
        public final String key;

        AUTH_TYPES(String key) {
            this.key = key;
        }
    }

    public LoginController(GoogleAuthenticationService googleAuthenticationService, FacebookAuthenticationService facebookAuthenticationService, PersonService personService) {
        this.googleAuthenticationService = googleAuthenticationService;
        this.facebookAuthenticationService = facebookAuthenticationService;
        this.personService = personService;
    }

    @GetMapping("/login")
    public ResponseEntity<Object> authenticate(HttpServletRequest request, HttpServletResponse response) {
        String authType = request.getParameter("authType");
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            if (authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring("Bearer ".length());
                /*
                 * Google
                 */
                if (authType.equals(AUTH_TYPES.GOOGLE.key)) {
                    GoogleIdToken.Payload idTokenPayload =  googleAuthenticationService.validateGoogleAuthToken(token);

                    Optional<PersonModel> matchingPerson = personService.getActivePersonByAuthId(idTokenPayload.getSubject());
                    PersonModel person =  matchingPerson.orElseGet(() -> personService.createPersonFromGoogleData(idTokenPayload));

                    if (person != null) {
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                }
                /*
                 * Facebook
                 */
                if (authType.equals(AUTH_TYPES.FACEBOOK.key)) {
                    FacebookTokenPayload idTokenPayload =  facebookAuthenticationService.validateFacebookAuthToken(token);

                    Optional<PersonModel> matchingPerson = personService.getActivePersonByAuthId(idTokenPayload.getSub());
                    PersonModel person =  matchingPerson.orElseGet(() -> personService.createPersonFromFacebookData(idTokenPayload));

                    if (person != null) {
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                }

                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
}
