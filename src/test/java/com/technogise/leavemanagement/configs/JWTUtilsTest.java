package com.technogise.leavemanagement.configs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import com.technogise.leavemanagement.entities.User;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JWTUtilsTest {
    
    private JWTUtils jwtUtils;
    private final String secret = "Testshshdhasjjascbhscbatestddskfjksdjfkdsdfdfdsdsdfdsjdsdfdsfdstestdsfdsfsdfdsfdsfdsdf";
    private Key key;

    @BeforeEach
    void setUp() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
        jwtUtils = new JWTUtils(secret);
    }

    @SuppressWarnings("deprecation")
    @Test
    @DisplayName("Given a user, when creating a token, then the token should not be null and contain the user's ID as the subject")
    void testCreateToken() {
        
        User user = new User();
        user.setId(1L);
        user.setName("Rick");

        String token = jwtUtils.createToken(user, false);
        assertNotNull(token);

        Claims claims = Jwts.parser()
        .setSigningKey(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();

        assertEquals(user.getId().toString(), claims.getSubject());
    }

    @Test
    @DisplayName("Given a valid token, when verifying and getting authentication, then the authentication should not be null, user should be authenticated, and user ID should match")
    void testVerifyAndGetAuthentication() {
        
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        String validToken = jwtUtils.createToken(user, false);

        Authentication authentication = jwtUtils.verifyAndGetAuthentication(validToken);
        assertNotNull(authentication, "Authentication should not be null for a valid token");
        assertTrue(authentication.isAuthenticated(), "User should be authenticated");
        assertEquals("1", authentication.getName(), "User ID should match");
    }    
}
