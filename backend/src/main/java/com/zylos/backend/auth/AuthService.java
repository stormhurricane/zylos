package com.zylos.backend.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zylos.backend.auth.dto.RegisterRequest;
import com.zylos.backend.features.user.UserRepository;
import com.zylos.backend.features.user.entity.User;
import com.zylos.backend.features.user.excption.UserAlreadyExistsException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor  
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional 
    public void register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.email())){
            throw new UserAlreadyExistsException("Email is already in use");
        }

        if(userRepository.existsByUsername(request.username())){
            throw new UserAlreadyExistsException("Username is already in use");
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.create(request.firstName(), request.lastName(), request.email(), request.username(), encodedPassword, request.role());

        userRepository.save(user);
    }
    
}
