package com.zylos.backend.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;
    private final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
    private final long expirationTime = 86400000; // 24 hours

    public String generateToken(String email, long userId, List<String> roles) {
        List<String> normalizedRoles = roles.stream()
                .map(this::normalizeRole)
                .collect(Collectors.toList());

        return Jwts.builder()
                .claim("userId", userId)
                .claim("roles", normalizedRoles)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    public ClaimsData extractClaimsData(String token) {
        final Claims claims = extractAllClaims(token);
        
        Object userIdClaim = claims.get("userId");
        long userId;
        if (userIdClaim instanceof Number number) {
            userId = number.longValue();
        } else if (userIdClaim instanceof String str) {
            userId = Long.parseLong(str);
        } else {
            throw new SecurityException("Missing or invalid 'userId' claim in JWT token");
        }

        UserPrincipal principal = new UserPrincipal(userId, claims.getSubject());

        Object roles = claims.get("roles");
        if (roles == null) roles = claims.get("role");

        List<SimpleGrantedAuthority> authorities = List.of();
        if (roles instanceof String roleStr) {
            authorities = List.of(new SimpleGrantedAuthority("ROLE_" + normalizeRole(roleStr)));
        } else if (roles instanceof List<?> roleList) {
            authorities = roleList.stream()
                    .filter(String.class::isInstance)
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + normalizeRole((String) role)))
                    .collect(Collectors.toList());
        }

        return new ClaimsData(principal, authorities);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            return !extractAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String normalizeRole(String role) {
        String r = role.toUpperCase();
        if ("TEACHER".equals(r)) return "INSTRUCTOR";
        return r;
    }

    public record ClaimsData(UserPrincipal principal, List<SimpleGrantedAuthority> authorities) {}
}