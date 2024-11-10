package com.radtimes.rad_times_server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Slf4j
@Service
public class GoogleAuthenticationService {

    private final PersonService personService;

    public GoogleAuthenticationService(PersonService personService) {
        this.personService = personService;
    }

    @Value("${oauth2.google.client-id-web}")
    private String CLIENT_ID_WEB;

    @Value("${oauth2.google.client-id-ios}")
    private String CLIENT_ID_IOS;

    public Payload validateGoogleAuthToken(String token) {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Arrays.asList(CLIENT_ID_WEB, CLIENT_ID_IOS))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                Payload payload = idToken.getPayload();
                String aud = (String) payload.getAudience();

                if (!aud.equals(CLIENT_ID_WEB) && !aud.equals(CLIENT_ID_IOS)) {
                    log.error("------------- Client ID mismatch in returned token");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
                }

                return payload;
            } else {
                log.error("------------- Provided token returned NULL on validation check");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
            }

        } catch (Exception e) {
            log.error("------------- Error validating provided token: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }
}
