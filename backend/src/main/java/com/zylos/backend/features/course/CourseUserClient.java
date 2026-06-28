package com.zylos.backend.features.course;

import com.zylos.backend.features.user.dto.UserResponse;
import java.util.List;
import java.util.Map;

public interface CourseUserClient {
    boolean existsById(long userId);

    // categorizes userIds with their roles
    Map<String, List<UserResponse>> categorizeUsersByIds(List<Long> userIds);
}