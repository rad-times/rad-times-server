package com.radtimes.rad_times_server.jwt_authorization;

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
    private static String REFRESH_SECRET_KEY;
    private static Long REFRESH_EXPIRATION_TIME;
    private static String ISSUER;

    public JWTUtil(@Value("${security.jwt.secret-key}") String key, @Value("${security.jwt.expiration-time}") Long time, @Value("${security.jwt.refresh-secret-key}") String refreshKey, @Value("${security.jwt.refresh-expiration-time}") Long refreshTime) {
        SECRET_KEY = key;
        EXPIRATION_TIME = time;
        REFRESH_SECRET_KEY = refreshKey;
        REFRESH_EXPIRATION_TIME = refreshTime;
        ISSUER = "https://radtimes.com";
    }

    public String createJWT(String email, PersonModel.LanguageLocale languageCode) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + EXPIRATION_TIME;
        Date exp = new Date(expMillis);

        JwtBuilder builder = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuedAt(now)
                .subject(email)
                .issuer(ISSUER)
                .expiration(exp)
                .claim("languageCode", languageCode)
                .signWith(getSigningKey());

        return builder.compact();
    }

    public String createRefreshToken(String email) {
        long expMillis = System.currentTimeMillis() + REFRESH_EXPIRATION_TIME;
        Date exp = new Date(expMillis);

        JwtBuilder builder = Jwts.builder()
                .subject(email)
                .issuer(ISSUER)
                .expiration(exp)
                .signWith(getRefreshTokenSigningKey());

        return builder.compact();
    }

    public Claims decodeJWT(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getRefreshTokenSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(REFRESH_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserEmailFromToken(String token, SecretKey signingKey) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getUserEmailFromJwtToken(String token) {
        return getUserEmailFromToken(token, getSigningKey());
    }

    public String getUserEmailFromRefreshToken(String token) {
        return getUserEmailFromToken(token, getRefreshTokenSigningKey());
    }

    public boolean validateToken(String authToken, SecretKey signingKey) {
        try {
            Jwts.parser()
                    .verifyWith(signingKey)
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

    public boolean validateJwtToken(String authToken) {
        return validateToken(authToken, getSigningKey());
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, getRefreshTokenSigningKey());
    }
}
