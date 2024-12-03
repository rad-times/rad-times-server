package com.radtimes.rad_times_server.util;

import com.radtimes.rad_times_server.model.PersonModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JWTUtilTest {

    @Autowired
    JWTUtil SECRET_KEY;

    @Autowired
    JWTUtil EXPIRATION_TIME;
    /*
     * Create a simple JWT, decode it, and assert the claims
     */
    @Test
    public void createAndDecodeJWT() {

        String userId = "12";
        String email = "david.ryan.hall@gmail.com";
        String jwtIssuer = "https://radtimes.com";
        PersonModel.LanguageLocale languageCode = PersonModel.LanguageLocale.EN;

        String jwt = JWTUtil.createJWT(
                userId,
                email,
                languageCode
        );

        Claims claims = JWTUtil.decodeJWT(jwt);

        assertEquals(PersonModel.LanguageLocale.EN.toString(), claims.get("languageCode"));
        assertEquals(email, claims.get("email"));
        assertEquals(jwtIssuer, claims.getIssuer());
        assertEquals(userId, claims.getSubject());
    }

    /*
     * Attempt to decode a bogus JWT and expect an exception
     */
    @Test
    public void decodeShouldFail() {
        String notAJwt = "This is not a JWT";
        assertThrows(MalformedJwtException.class, () -> JWTUtil.decodeJWT(notAJwt));
    }

    /*
     * Create a simple JWT, modify it, and try to decode it
     */
    @Test
    public void createAndDecodeTamperedJWT() {

        String userId = "12";
        String email = "david.ryan.hall@gmail.com";
        PersonModel.LanguageLocale languageCode = PersonModel.LanguageLocale.EN;

        String jwt = JWTUtil.createJWT(
                userId,
                email,
                languageCode
        );

        // tamper with the JWT
        StringBuilder tamperedJwt = new StringBuilder(jwt);
        tamperedJwt.setCharAt(22, 'I');

        assertNotEquals(jwt, tamperedJwt);

        // this will fail with a SignatureException
        assertThrows(SignatureException.class, () -> JWTUtil.decodeJWT(tamperedJwt.toString()));
    }
}
