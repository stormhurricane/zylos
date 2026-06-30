package com.zylos.backend.features.user.client;

import com.zylos.backend.features.course.CourseUserClient;
import com.zylos.backend.features.course.enrollment.dto.CourseParticipantsResponse;
import com.zylos.backend.features.user.Student;
import com.zylos.backend.features.user.Teacher;
import com.zylos.backend.features.user.User;
import com.zylos.backend.features.user.UserRepository;
import com.zylos.backend.features.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor 
public class CourseUserClientImpl implements CourseUserClient {

    private final UserRepository userRepository; 

    @Override
    public boolean existsById(long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public boolean isStudent(long userId) {
        return userRepository.isStudent(userId);
    }

    @Override
    public boolean isInstructor(long userId) {
        return userRepository.isInstructor(userId);
    }

    @Override
    @Transactional(readOnly = true) // FIX: Performance durch Read-Only Kontext
    public CourseParticipantsResponse categorizeUsersByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return new CourseParticipantsResponse(List.of(), List.of());
        }
        List<User> users = userRepository.findAllByIdWithSubtypes(userIds);

        Map<Boolean, List<UserResponse>> partitioned = users.stream()
                .collect(Collectors.partitioningBy(
                        user -> user instanceof Teacher,
                        Collectors.mapping(UserResponse::new, Collectors.toList())
                ));

        return new CourseParticipantsResponse(
            partitioned.getOrDefault(true, List.of()), 
            partitioned.getOrDefault(false, List.of())
        );
    }
}