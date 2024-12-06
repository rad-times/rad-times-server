package com.radtimes.rad_times_server.jwt_authorization;

import com.radtimes.rad_times_server.model.PersonModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JWTUtilTest {
    private final String notARealKey = "lksd64lkjerg345lkjelk653gjelkjglkf345jgldkfjgoierjgoij3409tkn0eu90vn934v98n34890n3489gn4";
    private final String notARealRefreshKey = "lksd64lkjerg345lkjelk653gjelkjglkf345jgldkfjgoierjgoij3409tkn0eu90vn934v98n34890n3489gn4";
    private final JWTUtil jwtUtil = new JWTUtil(notARealKey, 1000L, notARealRefreshKey, 5000L);

    private final String userEmail = "app_user@email.com";
    private final PersonModel.LanguageLocale languageCode = PersonModel.LanguageLocale.EN;
    /*
     * Create a simple JWT, decode it, and assert the claims
     */
    @Test
    public void createAndDecodeJWT() {
        String jwtIssuer = "https://radtimes.com";


        String jwt = jwtUtil.createJWT(
                userEmail,
                languageCode
        );

        Claims claims = jwtUtil.decodeJWT(jwt);

        assertEquals(PersonModel.LanguageLocale.EN.toString(), claims.get("languageCode"));
        assertEquals(jwtIssuer, claims.getIssuer());
        assertEquals(userEmail, claims.getSubject());
    }

    /*
     * Attempt to decode a bogus JWT and expect an exception
     */
    @Test
    public void decodeShouldFail() {
        String notAJwt = "This is not a JWT";
        assertThrows(MalformedJwtException.class, () -> jwtUtil.decodeJWT(notAJwt));
    }

    @Test
    public void getUsersEmailTest() {
        String jwt = jwtUtil.createJWT(
                userEmail,
                languageCode
        );
        String email = jwtUtil.getUserEmailFromJwtToken(jwt);
        assertEquals(userEmail, email);
    }

    @Test
    public void validateTokenTest() {
        String jwt = jwtUtil.createJWT(
                userEmail,
                languageCode
        );
        boolean isValid = jwtUtil.validateJwtToken(jwt);
        assertTrue(isValid);

        boolean shouldNotBeValid = jwtUtil.validateJwtToken("wejkfnwjefniwenfiuwenfiuwenf");
        assertFalse(shouldNotBeValid);
    }

    /*
     * Create a simple JWT, modify it, and try to decode it
     */
    @Test
    public void createAndDecodeTamperedJWT() {

        PersonModel.LanguageLocale languageCode = PersonModel.LanguageLocale.EN;

        String jwt = jwtUtil.createJWT(
                userEmail,
                languageCode
        );

        // tamper with the JWT
        StringBuilder tamperedJwt = new StringBuilder(jwt);
        tamperedJwt.setCharAt(22, 'I');

        assertNotEquals(jwt, tamperedJwt);

        // this will fail with a SignatureException
        assertThrows(SignatureException.class, () -> jwtUtil.decodeJWT(tamperedJwt.toString()));
    }
}
