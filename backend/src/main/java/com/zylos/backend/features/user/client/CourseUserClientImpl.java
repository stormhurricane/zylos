package com.zylos.backend.features.user.client;

import com.zylos.backend.features.course.CourseUserClient;
import com.zylos.backend.features.user.Student;
import com.zylos.backend.features.user.Teacher;
import com.zylos.backend.features.user.User;
import com.zylos.backend.features.user.UserRepository;
import com.zylos.backend.features.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor 
public class CourseUserClientImpl implements CourseUserClient {

    private final UserRepository userRepository; 

    @Override
    public boolean existsById(long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public Map<String, List<UserResponse>> categorizeUsersByIds(List<Long> userIds) {
        Map<String, List<UserResponse>> result = new HashMap<>();
        List<UserResponse> instructors = new ArrayList<>();
        List<UserResponse> students = new ArrayList<>();

        if (userIds == null || userIds.isEmpty()) {
            result.put("instructors", instructors);
            result.put("students", students);
            return result;
        }

        List<User> users = userRepository.findAllByIdWithSubtypes(userIds);

        for (User user : users) {
            UserResponse responseDto = new UserResponse(user);

            if (user instanceof Teacher) {
                instructors.add(responseDto);
            } else if (user instanceof Student) {
                students.add(responseDto);
            }
        }

        result.put("instructors", instructors);
        result.put("students", students);
        return result;
    }
}