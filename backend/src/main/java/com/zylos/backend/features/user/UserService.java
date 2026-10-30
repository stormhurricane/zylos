package com.zylos.backend.features.user;

import com.zylos.backend.features.user.dto.UserPrivateResponse;
import com.zylos.backend.features.user.dto.UserPublicResponse;
import com.zylos.backend.features.user.entity.User;
import com.zylos.backend.features.user.excption.UserNotFoundException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor  
public class UserService {
    
    private final UserRepository userRepository;

    public UserPrivateResponse getPrivateProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return new UserPrivateResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getUsername(),
            user.getEmail()
        );
    }

    public UserPublicResponse getPublicProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return new UserPublicResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getUsername()
        );
    }

}
