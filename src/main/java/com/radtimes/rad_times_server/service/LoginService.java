package com.radtimes.rad_times_server.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.radtimes.rad_times_server.jwt_authorization.JWTUtil;
import com.radtimes.rad_times_server.model.PersonModel;
import com.radtimes.rad_times_server.model.oauth.FacebookTokenPayload;
import com.radtimes.rad_times_server.service.oauth.FacebookAuthenticationService;
import com.radtimes.rad_times_server.service.oauth.GoogleAuthenticationService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
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

    public Map<String, Object> createAuthTokenPair(PersonModel person) {
        String userToken = jwtUtil.createJWT(person.getEmail(), person.getLanguage_code());
        String refreshToken = jwtUtil.createRefreshToken(person.getEmail());

        final Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", userToken);
        tokens.put("refreshToken", refreshToken);
        personService.saveRefreshToken(refreshToken, person.getEmail());
        return tokens;
    }

    public PersonModel googleLogin(String token) {
        GoogleIdToken.Payload idTokenPayload =  googleAuthenticationService.validateGoogleAuthToken(token);

        Optional<PersonModel> matchingPerson = personService.findPersonByEmail(idTokenPayload.getEmail());
        return matchingPerson.orElseGet(() -> personService.createPersonFromGoogleData(idTokenPayload));
    }

    public PersonModel facebookLogin(String token) {
        FacebookTokenPayload idTokenPayload =  facebookAuthenticationService.validateFacebookAuthToken(token);

        Optional<PersonModel> matchingPerson = personService.findPersonByEmail(idTokenPayload.getEmail());
        return matchingPerson.orElseGet(() -> personService.createPersonFromFacebookData(idTokenPayload));
    }
}
