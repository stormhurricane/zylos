package com.zylos.backend.config.security;

import org.springframework.security.test.context.support.WithSecurityContext;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserPrincipalSecurityContextFactory.class)
public @interface WithMockUserPrincipal {
    long id() default 1L;
    String email() default "instructor@test.com";
    String role() default "TEACHER";
}