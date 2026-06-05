package libraryms.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service    // Marks this as a Spring-managed service
public class JwtService {

    @Value("${jwt.secret}")        // Reads from application.properties
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Create the signing key from our secret string
    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate a token for a username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    // Read the username from a token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Check if a token is still valid
    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;    // Token expired or tampered with
        }
    }
}
