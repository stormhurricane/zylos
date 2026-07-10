package com.zylos.backend.features.projectgroup.todo.dto;

import java.time.LocalDateTime;

import com.zylos.backend.features.projectgroup.todo.ProjectGroupTodo;

public record TodoResponse(
    Long id,
    Long groupId,
    String title,
    Long assignedToUserId,
    boolean isCompleted,
    LocalDateTime createdAt
) {
    public static TodoResponse fromEntity(ProjectGroupTodo todo) {
        return new TodoResponse(
            todo.getId(),
            todo.getProjectGroup().getId(),
            todo.getTitle(),
            todo.getAssignedToUserId(),
            todo.isCompleted(),
            todo.getCreatedAt()
        );
    }
}