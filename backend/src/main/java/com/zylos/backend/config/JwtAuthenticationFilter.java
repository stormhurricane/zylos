package com.zylos.backend.config;

import com.zylos.backend.service.JwtService;
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
            logger.warn("No Bearer token found in request to {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.isTokenValid(jwt)) {
                // Extrahiere und normalisiere Rollen (String oder List, "role" oder "roles")
                List<SimpleGrantedAuthority> authorities = jwtService.extractClaim(jwt, claims -> {
                    Object roles = claims.get("roles");
                    if (roles == null) roles = claims.get("role");

                    if (roles instanceof String roleStr) {
                        return List.of(new SimpleGrantedAuthority("ROLE_" + normalizeRole(roleStr)));
                    } else if (roles instanceof List<?> roleList) {
                        return roleList.stream()
                                .filter(String.class::isInstance)
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + normalizeRole((String) role)))
                                .collect(Collectors.toList());
                    }
                    return List.<SimpleGrantedAuthority>of();
                });

                logger.info("User {} authenticated with authorities: {}", userEmail, authorities);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail, null, authorities);
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
    private String normalizeRole(String role) {
        String r = role.toUpperCase();
        if ("TEACHER".equals(r)) return "INSTRUCTOR";
        return r;
    }
}