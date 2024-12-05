package com.radtimes.rad_times_server.util;

import javax.crypto.SecretKey;

import com.radtimes.rad_times_server.model.PersonModel;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JWTUtil {

    private static String SECRET_KEY;
    private static Long EXPIRATION_TIME;


    public JWTUtil(@Value("${security.jwt.secret-key}") String key, @Value("${security.jwt.expiration-time}") Long time) {
        SECRET_KEY = key;
        EXPIRATION_TIME = time;
    }

    public String createJWT(String subjectId, String email, PersonModel.LanguageLocale languageCode) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + EXPIRATION_TIME;
        Date exp = new Date(expMillis);

        JwtBuilder builder = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuedAt(now)
                .subject(subjectId)
                .issuer("https://radtimes.com")
                .expiration(exp)
                .claim("languageCode", languageCode)
                .claim("email", email)
                .signWith(getSigningKey());

        return builder.compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserEmailFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
