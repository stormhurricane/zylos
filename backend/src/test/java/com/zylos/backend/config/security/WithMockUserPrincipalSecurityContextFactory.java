package com.zylos.backend.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockUserPrincipalSecurityContextFactory implements WithSecurityContextFactory<WithMockUserPrincipal> {
    @Override
    public SecurityContext createSecurityContext(WithMockUserPrincipal annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        
        // Nutzt deinen echten UserPrincipal Record
        UserPrincipal principal = new UserPrincipal(annotation.id(), annotation.email());
        
        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal, 
                "password", 
                List.of(new SimpleGrantedAuthority("ROLE_" + annotation.role()))
        );
        
        context.setAuthentication(auth);
        return context;
    }
}