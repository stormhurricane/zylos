package com.zylos.backend.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // logger.warn("No Bearer token found in request to {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
 
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.isTokenValid(jwt)) {
                // 1. Extrahiere die numerische ID aus dem JWT (Muss als Claim existieren!)
                // Falls dein JwtService eine generische extractClaim-Methode hat:
                Object userIdClaim = jwtService.extractClaim(jwt, claims -> claims.get("userId"));

                long userId = 0L;
                if (userIdClaim instanceof Number number) {
                    userId = number.longValue(); // Wandelt Integer, Long, etc. sicher in ein primitives long um!
                } else if (userIdClaim instanceof String str) {
                    userId = Long.parseLong(str); // Sicher ist sicher, falls es als String codiert wurde
                } else {
                    // [Likely] Wenn das fehlschlägt, ist das Token korrupt oder nicht für diesen Context gebaut
                    throw new SecurityException("Missing or invalid 'userId' claim in JWT token");
                }

                // Rollen-Extraktion bleibt exakt gleich...
                List<SimpleGrantedAuthority> authorities = jwtService.extractClaim(jwt, claims -> {
                    Object roles = claims.get("roles");
                    if (roles == null) roles = claims.get("role");
                    if (roles instanceof String roleStr) return List.of(new SimpleGrantedAuthority("ROLE_" + normalizeRole(roleStr)));
                    if (roles instanceof List<?> roleList) {
                        return roleList.stream().filter(String.class::isInstance).map(role -> new SimpleGrantedAuthority("ROLE_" + normalizeRole((String) role))).collect(Collectors.toList());
                    }
                    return List.<SimpleGrantedAuthority>of();
                });

                // Da userId ein primitives long ist, fällt die null-Prüfung komplett weg!
                UserPrincipal principal = new UserPrincipal(userId, userEmail);
                
                // 3. Übergib das principal-Objekt statt nur der E-Mail
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        principal, null, authorities); // Principal ist jetzt das Record!
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Normalisiert Rollenbezeichnungen, um Diskrepanzen zwischen 
     * TEACHER und INSTRUCTOR zu vermeiden.
     */
    // TODO Rollen konsistent benennen, damit diese Normalisierung überflüssig wird
    private String normalizeRole(String role) {
        String r = role.toUpperCase();
        if ("TEACHER".equals(r)) return "INSTRUCTOR";
        return r;
    }
}