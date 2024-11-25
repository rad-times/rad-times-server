package com.radtimes.rad_times_server.service.oauth;

import com.google.gson.Gson;
import com.radtimes.rad_times_server.model.oauth.FacebookTokenPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.apache.commons.codec.binary.Base64;

import java.util.Date;

@Slf4j
@Service
public class FacebookAuthenticationService {

    public FacebookAuthenticationService() {}

    @Value("${oauth2.facebook.client-id}")
    private String CLIENT_ID;

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public FacebookTokenPayload validateFacebookAuthToken(String token) {
        try {
            String[] split_string = token.split("\\.");;
            String base64EncodedBody = split_string[1];

            Base64 base64Url = new Base64(true);
            String body = new String(base64Url.decode(base64EncodedBody));

            Gson g = new Gson();
            FacebookTokenPayload payload = g.fromJson(body, FacebookTokenPayload.class);
            String aud = payload.getAud();
            String iss = payload.getIss();
            String secret = payload.getNonce();
            Long exp = payload.getExp();
            Date expDate = new Date(exp * 1000);

             // Full validation https://developers.facebook.com/docs/facebook-login/limited-login/token/validating
             if (!aud.equals(CLIENT_ID)) {
                 log.error("------------- Client ID mismatch in returned token");
                 throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
             }

             if (!iss.equals("https://www.facebook.com")) {
                log.error("------------- Issuer mismatch in returned token");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
             }

             if (!secret.equals(SECRET_KEY)) {
                 log.error("------------- Secret key mismatch in returned token");
                 throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
             }

            if (expDate.before(new Date())) {
                log.error("------------- Token expired");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
            }

            return payload;

        } catch (Exception e) {
            log.error("------------- Error validating provided token: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }
}
