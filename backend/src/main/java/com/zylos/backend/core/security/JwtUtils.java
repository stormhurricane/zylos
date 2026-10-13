package com.zylos.backend.core.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component 
public class JwtUtils {
    
    private String jwtSecret;
    private long jwtExpirationMs; 
    private String jwtTokenName;

    public JwtUtils(
            @Value("${app.jwt.secret:"
                    + "einSehrLangerUndSichererGeheimerSchluesselMitMindestens32Zeichen}") 
            String jwtSecret, 
            @Value("${app.jwt.expiration-ms:86400000}") long jwtExpirationMs, 
            @Value("${app.jwt.cookie-name:jwt}") String jwtTokenName) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
        this.jwtTokenName = jwtTokenName;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(jwtTokenName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public ResponseCookie generateJwtCookie(UserDetails userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        return ResponseCookie.from(jwtTokenName, jwt)
            .path("/")
            .maxAge(jwtExpirationMs / 1000)
            .httpOnly(true)
            .secure(false) // unsure if HTTPS
            .sameSite("lax")
            .build();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtTokenName, "")
            .path("/")
            .maxAge(0)
            .httpOnly(true)
            .build();
    }

    private String generateTokenFromUsername(String username) {
        return Jwts.builder()
            .subject(username)
            .issuedAt(new Date())
            .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(getSigningKey())
            .compact();
        
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
