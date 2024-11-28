package com.radtimes.rad_times_server.util;

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

        Integer userId = 12;
        String jwtIssuer = "JWT Demo";
        String jwtSubject = "Andrew";

        String jwt = JWTUtil.createJWT(
                jwtIssuer,
                jwtSubject,
                userId
        );

        Claims claims = JWTUtil.decodeJWT(jwt);

        assertEquals(userId, claims.get("userId"));
        assertEquals(jwtIssuer, claims.getIssuer());
        assertEquals(jwtSubject, claims.getSubject());
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

        Integer userId = 12;
        String jwtIssuer = "JWT Demo";
        String jwtSubject = "Andrew";

        String jwt = JWTUtil.createJWT(
                jwtIssuer,
                jwtSubject,
                userId
        );

        // tamper with the JWT
        StringBuilder tamperedJwt = new StringBuilder(jwt);
        tamperedJwt.setCharAt(22, 'I');

        assertNotEquals(jwt, tamperedJwt);

        // this will fail with a SignatureException
        assertThrows(SignatureException.class, () -> JWTUtil.decodeJWT(tamperedJwt.toString()));
    }
}
