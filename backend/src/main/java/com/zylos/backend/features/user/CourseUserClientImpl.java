package com.zylos.backend.features.user;

import com.zylos.backend.features.course.CourseUserClient;
import com.zylos.backend.features.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor 
class CourseUserClientImpl implements CourseUserClient {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

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

        // get all user data from DB
        List<User> users = userRepository.findAllById(userIds);

        // categorize by existence in sub tables
        for (User user : users) {
            UserResponse responseDto = new UserResponse(user);

            // Prüfen, ob der User als Teacher oder Student existiert
            if (teacherRepository.existsByUserId(user.getId())) {
                instructors.add(responseDto);
            } else if (studentRepository.existsByUserId(user.getId())) {
                students.add(responseDto);
            } else {
                // TODO: Fallback: if neither student nor teacher, log an error?
            }
        }

        result.put("instructors", instructors);
        result.put("students", students);
        return result;
    }
}