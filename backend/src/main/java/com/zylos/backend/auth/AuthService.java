package com.zylos.backend.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zylos.backend.auth.dto.AuthResult;
import com.zylos.backend.auth.dto.LoginRequest;
import com.zylos.backend.auth.dto.RegisterRequest;
import com.zylos.backend.auth.dto.UserResponse;
import com.zylos.backend.core.security.CustomUserDetails;
import com.zylos.backend.core.security.JwtUtils;
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
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

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

    public AuthResult login(LoginRequest loginData) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginData.identifier(), 
                loginData.password()
            )
        );

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        ResponseCookie cookie = jwtUtils.generateJwtCookie(userDetails);
        UserResponse userResponse = new UserResponse(
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            userDetails.getRoles()
        );
        return new AuthResult(
            cookie,
            userResponse
        );
    }

    public ResponseCookie logout(){
        return jwtUtils.getCleanJwtCookie();
    }
    
}
