package com.zylos.backend.features.course.enrollment.dto;

import java.util.List;

import com.zylos.backend.features.user.dto.UserResponse;

public record CourseParticipantsResponse(
    List<UserResponse> instructors,
    List<UserResponse> students
) {}
