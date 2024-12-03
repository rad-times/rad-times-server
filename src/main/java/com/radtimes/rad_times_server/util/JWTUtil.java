package com.radtimes.rad_times_server.util;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

import com.radtimes.rad_times_server.model.PersonModel;
import io.jsonwebtoken.*;
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

    public static String createJWT(String subjectId, String email, PersonModel.LanguageLocale languageCode) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + EXPIRATION_TIME;
        Date exp = new Date(expMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setSubject(subjectId)
                .setIssuer("https://radtimes.com")
                .setExpiration(exp)
                .claim("languageCode", languageCode)
                .claim("email", email)
                .signWith(signatureAlgorithm, signingKey);

        return builder.compact();
    }

    public static Claims decodeJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
    }

}
