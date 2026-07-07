package com.zylos.backend.config.web;

import com.zylos.backend.config.security.UserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrentUserIdArgumentResolverTest {

    private final CurrentUserIdArgumentResolver resolver = new CurrentUserIdArgumentResolver();
    
    private MethodParameter mockParameter;
    private NativeWebRequest mockWebRequest;

    @BeforeEach
    void setUp() {
        mockParameter = Mockito.mock(MethodParameter.class);
        mockWebRequest = Mockito.mock(NativeWebRequest.class);
        SecurityContextHolder.clearContext(); 
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldResolveUserIdWhenUserPrincipalIsPresent() throws Exception {
        UserPrincipal principal = new UserPrincipal(42L, "test@zylos.com");
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", List.of());
        
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        Object result = resolver.resolveArgument(mockParameter, null, mockWebRequest, null);

        // Assert
        assertEquals(42L, result);
    }

    @Test
    void shouldThrowInsufficientAuthenticationExceptionWhenNoPrincipalPresent() {
        SecurityContextHolder.clearContext();

        // Act & Assert
        assertThrows(InsufficientAuthenticationException.class, () -> 
            resolver.resolveArgument(mockParameter, null, mockWebRequest, null)
        );
    }

    @Test
    void shouldThrowInsufficientAuthenticationExceptionWhenPrincipalIsWrongType() {
        Authentication auth = new UsernamePasswordAuthenticationToken("anonymousUser", "password", List.of());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // Act & Assert
        assertThrows(InsufficientAuthenticationException.class, () -> 
            resolver.resolveArgument(mockParameter, null, mockWebRequest, null)
        );
    }
}