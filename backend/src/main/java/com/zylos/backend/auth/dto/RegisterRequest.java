package com.zylos.backend.auth.dto;

import com.zylos.backend.auth.validation.ValidRegistrationRole;
import com.zylos.backend.features.user.SystemRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, message = "First name must be at least 2 characters long")
    String firstName,
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, message = "Last name must be at least 2 characters long")
    String lastName,
    
    @NotBlank(message = "Email is required")
    @Email (message = "Email should be valid")
    String email,
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String password,
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Username must be at least 3 characters long")
    String username,

    @NotNull(message = "Role is required")
    @ValidRegistrationRole 
    SystemRole role
) {}