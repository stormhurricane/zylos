package com.zylos.backend.auth.dto;

public record RegisterRequest(
    String firstName,
    String lastName,
    String email,
    String password,
    String username
) {}