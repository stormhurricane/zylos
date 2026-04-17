package com.zylos.backend.model.dto;

import java.util.List;

public record CourseParticipantsResponse(
    List<UserResponse> instructors,
    List<UserResponse> students
) {}
