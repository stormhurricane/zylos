package com.zylos.backend.features.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zylos.backend.features.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
    
}
