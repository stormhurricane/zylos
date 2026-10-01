package com.zylos.backend.auth;

import java.util.HashMap;
import java.util.Map;

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
        boolean emailAlreadyTaken = userRepository.existsByEmail(request.email());
        boolean userNameAlreadyTaken = userRepository.existsByUsername(request.username());

        Map<String,String> errors = new HashMap<>();
        if(emailAlreadyTaken){
            errors.put("email", "Email is already in use");
        }
        if(userNameAlreadyTaken){
            errors.put("username", "Username is already in use");
        }

        if(!errors.isEmpty()){
            throw new UserAlreadyExistsException(errors, "Registration failed due to conflicting user data");
        }
        

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.create(request.firstName(), request.lastName(), request.email(), request.username(), encodedPassword, request.role());

        userRepository.save(user);
    }
    
}
