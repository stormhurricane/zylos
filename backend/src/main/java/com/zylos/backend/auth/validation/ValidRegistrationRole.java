package com.zylos.backend.auth.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target ({ElementType.FIELD, ElementType.PARAMETER})
@Retention (RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RegistrationRoleValidator.class)
@Documented 
public @interface ValidRegistrationRole {
    
    String message() default "This role is not allowed for registration";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
