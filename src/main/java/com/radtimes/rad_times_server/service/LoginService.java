package com.radtimes.rad_times_server.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.radtimes.rad_times_server.jwt_authorization.JWTUtil;
import com.radtimes.rad_times_server.model.PersonModel;
import com.radtimes.rad_times_server.model.oauth.FacebookTokenPayload;
import com.radtimes.rad_times_server.service.oauth.FacebookAuthenticationService;
import com.radtimes.rad_times_server.service.oauth.GoogleAuthenticationService;
import jakarta.persistence.Entity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final JWTUtil jwtUtil;
    private final PersonService personService;
    private final GoogleAuthenticationService googleAuthenticationService;
    private final FacebookAuthenticationService facebookAuthenticationService;

    public LoginService(JWTUtil jwtUtil, PersonService personService, GoogleAuthenticationService googleAuthenticationService, FacebookAuthenticationService facebookAuthenticationService) {
        this.jwtUtil = jwtUtil;
        this.personService = personService;
        this.googleAuthenticationService = googleAuthenticationService;
        this.facebookAuthenticationService = facebookAuthenticationService;
    }

    @Data
    public static class TokenPair {
        private String accessToken;
        private String refreshToken;
    }

    /**
     * Pull the token out of the request header
     */
    public String getBearerToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            if (authorizationHeader.startsWith("Bearer ")) {
                return authorizationHeader.substring("Bearer ".length());
            }
        }
        return null;
    }

    /**
     * Create a response ready object with the access token and refresh token
     */
    public TokenPair createAuthTokenPair(PersonModel person) {
        String userToken = jwtUtil.createJWT(person.getEmail(), person.getLanguage_code());
        String refreshToken = jwtUtil.createRefreshToken(person.getEmail());

        final TokenPair tokens = new TokenPair();
        tokens.setAccessToken(userToken);
        tokens.setRefreshToken(refreshToken);
        personService.saveRefreshToken(refreshToken, person.getEmail());
        return tokens;
    }

    /**
     * Google
     */
    public PersonModel googleLogin(String token) {
        GoogleIdToken.Payload idTokenPayload =  googleAuthenticationService.validateGoogleAuthToken(token);

        Optional<PersonModel> matchingPerson = personService.findPersonByEmail(idTokenPayload.getEmail());
        return matchingPerson.orElseGet(() -> personService.createPersonFromGoogleData(idTokenPayload));
    }
    /**
     * Facebook
     */
    public PersonModel facebookLogin(String token) {
        FacebookTokenPayload idTokenPayload =  facebookAuthenticationService.validateFacebookAuthToken(token);

        Optional<PersonModel> matchingPerson = personService.findPersonByEmail(idTokenPayload.getEmail());
        return matchingPerson.orElseGet(() -> personService.createPersonFromFacebookData(idTokenPayload));
    }

    /**
     * Handle request to get a new access token via a refresh token
     */
    public TokenPair getRefreshedTokenPair (String refreshToken) {
        String email = jwtUtil.getUserEmailFromRefreshToken(refreshToken);

        Optional<PersonModel> person = personService.findPersonByEmail(email);
        if (person.isPresent()) {
            Optional<String> savedToken = personService.getRefreshToken(email);

            if (savedToken.isPresent()) {
                if (savedToken.get().equals(refreshToken)) {
                    return this.createAuthTokenPair(person.get());
                } else {
                    // If the stored token does not match the most recent token for the user, clear out
                    // the stored token to force the user to re-authenticate
                    personService.clearRefreshToken(email);
                }
            }
        }
        return null;
    }
}
