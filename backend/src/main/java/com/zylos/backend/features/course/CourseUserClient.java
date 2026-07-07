package com.zylos.backend.features.course;

import com.zylos.backend.features.course.enrollment.dto.CourseParticipantsResponse;
import java.util.List;

public interface CourseUserClient {
    boolean existsById(long userId);

    boolean isStudent(long userId);
    boolean isInstructor(long userId);

    // categorizes userIds with their roles
    CourseParticipantsResponse categorizeUsersByIds(List<Long> userIds);
}