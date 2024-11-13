package com.amazon.test_task.auth.jwt;

import com.amazon.test_task.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Service for handling JWT operations, such as generating and parsing tokens.
@Service
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret; // Secret key for signing the JWT.

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime; // Duration after which the token expires.

    // Generates a JWT token for a given user with user-specific claims.
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = user.getRoles().stream().map(role -> role.getName()).toList();
        claims.put("id", user.getId()); // User ID claim.
        claims.put("roles", roles); // User roles claim.
        claims.put("email", user.getEmail()); // User email claim.

        Date issuedAt = new Date(); // Token issue date.
        Date expiration = new Date(issuedAt.getTime() + jwtLifetime.toMillis()); // Expiration time.
        return Jwts.builder()
                .claims(claims)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSigningKey()) // Sign the token with the secret key.
                .compact(); // Build the token as a compact, URL-safe string.
    }

    // Retrieves the signing key for JWT based on the secret.
    private Key getSigningKey() {
        byte[] keyBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Parses the JWT token to retrieve claims such as ID, roles, and email.
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload(); // Return the claims payload of the token.
    }

    // Extracts the user ID from the token.
    public Long getUserId(String token) {
        return Long.parseLong(getClaims(token).get("id", String.class));
    }

    // Extracts the email from the token.
    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    // Extracts the roles from the token as a list of strings.
    public List<String> getRoles(String token) {
        return getClaims(token).get("roles", List.class);
    }
}
