package com.zylos.backend.core.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
    public UserDetails loadUserByUsername(String username){
        User user = userRepository.findByUsernameOrEmail(username, username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    

        String roleName = "ROLE_" + user.getRole().name();

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority(roleName))
        );
    }

}
