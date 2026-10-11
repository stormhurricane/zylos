package com.zylos.backend.core.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zylos.backend.features.user.UserRepository;
import com.zylos.backend.features.user.entity.User;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;

    @Override 
    public CustomUserDetails loadUserByUsername(String username){
        User user = userRepository.findByUsernameOrEmail(username, username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    
        return new CustomUserDetails(user);
    }

}
