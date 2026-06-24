package com.zylos.backend.features.course;

import com.zylos.backend.features.user.dto.UserResponse;
import java.util.List;
import java.util.Map;

public interface CourseUserClient {
    boolean existsById(long userId);
    // returns a Map, that associates IDs with UserResponses
    Map<String, List<UserResponse>> categorizeUsersByIds(List<Long> userIds);
}