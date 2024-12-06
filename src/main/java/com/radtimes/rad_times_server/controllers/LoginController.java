package com.radtimes.rad_times_server.controllers;

import com.radtimes.rad_times_server.model.PersonModel;
import com.radtimes.rad_times_server.service.LoginService;
import com.radtimes.rad_times_server.service.PersonService;
import com.radtimes.rad_times_server.jwt_authorization.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class LoginController {
    private final LoginService loginService;
    private final PersonService personService;
    private final JWTUtil jwtUtil;

    private enum AUTH_TYPES {
        GOOGLE("google"),
        FACEBOOK("facebook");
        public final String key;

        AUTH_TYPES(String key) {
            this.key = key;
        }
    }

    public LoginController(LoginService loginService, PersonService personService, JWTUtil jwtUtil) {
        this.loginService = loginService;
        this.personService = personService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticate(HttpServletRequest request, HttpServletResponse response) {
        String authType = request.getParameter("authType");
        String token = loginService.getBearerToken(request);
        PersonModel person = null;

        if (token != null) {
            /*
             * Google
             */
            if (authType.equals(AUTH_TYPES.GOOGLE.key)) {
                person = loginService.googleLogin(token);
            }
            /*
             * Facebook
             */
            if (authType.equals(AUTH_TYPES.FACEBOOK.key)) {
                person = loginService.facebookLogin(token);
            }

            if (person != null) {
                Map<String, Object> tokens = loginService.createAuthTokenPair(person);
                return new ResponseEntity<>(tokens, HttpStatus.OK);
            }

            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/validateToken")
    public ResponseEntity<Void> validateToken(HttpServletRequest request) {
        String token = loginService.getBearerToken(request);
        if (token != null) {
            boolean isValid = jwtUtil.validateJwtToken(token);
            if (isValid) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/refreshAccessToken")
    public ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request) {
        String refreshToken = loginService.getBearerToken(request);
        if (refreshToken != null) {
            Map<String, Object> tokens = loginService.getRefreshedTokenPair(refreshToken);
            if (tokens != null) {
                return new ResponseEntity<>(tokens, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
}
