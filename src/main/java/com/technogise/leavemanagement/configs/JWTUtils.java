package com.technogise.leavemanagement.configs;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import com.technogise.leavemanagement.entities.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
@Component
public class JWTUtils {

    private static final long TOKEN_VALIDITY = 86400000L;
    private static final long TOKEN_VALIDITY_REMEMBER = 2592000000L;
    private final Key key;

    public JWTUtils(@Value("${app.jwtSecret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(User user, boolean rememberMe) {
        long now = (new Date()).getTime();
        Date validity = rememberMe ? new Date(now + TOKEN_VALIDITY_REMEMBER) : new Date(now + TOKEN_VALIDITY);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRoles());
    
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .addClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}

