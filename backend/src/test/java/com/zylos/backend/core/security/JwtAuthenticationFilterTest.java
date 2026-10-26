package com.zylos.backend.core.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith (MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    
    @Mock 
    private JwtUtils jwtUtils;
    @Mock 
    private CustomUserDetailsService userDetailsService;
    @Mock 
    private HttpServletRequest request;
    @Mock 
    private HttpServletResponse response;
    @Mock 
    private FilterChain filterChain;
    @InjectMocks 
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach 
    public void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test 
    public void shouldSetAuthenticationWhenValidCookieAndToken() 
            throws ServletException, IOException {

        String token = "valid.jwt.token";
        String username = "testuser";
        when(jwtUtils.getJwtFromCookie(request)).thenReturn(token);
        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUsernameFromJwtToken(token)).thenReturn(username);

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_STUDENT")))
                .when(userDetails)
                .getAuthorities();

        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(), userDetails);
        verify(filterChain).doFilter(request, response);
    }

    @Test 
    public void shouldNotSetAuthWhenTokenIsInvalid() throws ServletException, IOException {
        String invalidToken = "invalid.jwt.token";
        when(jwtUtils.getJwtFromCookie(request)).thenReturn(invalidToken);
        when(jwtUtils.validateJwtToken(invalidToken)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test 
    public void shouldNotSetAuthWhenCookieIsMissing() throws ServletException, IOException {
        when(jwtUtils.getJwtFromCookie(request)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test 
    public void shouldNotSetAuthWhenExceptionIsThrown() throws ServletException, IOException {
        String brokenString = "malformed.jwt.token";
        when(jwtUtils.getJwtFromCookie(request)).thenReturn(brokenString);
        when(jwtUtils.validateJwtToken(brokenString))
                .thenThrow(new RuntimeException("Token pasring error"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test 
    public void shouldNotSetAuthWhenUserNotFound() throws ServletException, IOException {
        String token = "valid.jwt.token";
        String username = "testuser";
        when(jwtUtils.getJwtFromCookie(request)).thenReturn(token);
        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUsernameFromJwtToken(token)).thenReturn(username);

        when(userDetailsService.loadUserByUsername(username))
                .thenThrow(new UsernameNotFoundException("User not found"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}