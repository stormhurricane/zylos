package com.zylos.backend.features.projectgroup.todo;

import com.zylos.backend.config.web.CurrentUserId;
import com.zylos.backend.features.projectgroup.todo.dto.TodoRequest;
import com.zylos.backend.features.projectgroup.todo.dto.TodoResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-groups/{groupId}/todos")
@PreAuthorize("hasRole('STUDENT')") // Basis-Absicherung
@RequiredArgsConstructor
public class ProjectGroupTodoController {

    private final ProjectGroupTodoService projectGroupTodoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResponse createTodo(
            @PathVariable Long groupId,
            @Valid @RequestBody TodoRequest request,
            @CurrentUserId long currentUserId
    ) {
        return projectGroupTodoService.createTodo(groupId, request, currentUserId);
    }

    @GetMapping
    public List<TodoResponse> getTodos(
            @PathVariable Long groupId,
            @CurrentUserId long currentUserId
    ) {
        return projectGroupTodoService.getTodos(groupId, currentUserId);
    }

    @PatchMapping("/{todoId}/toggle")
    public TodoResponse toggleTodo(
            @PathVariable Long groupId,
            @PathVariable Long todoId,
            @CurrentUserId long currentUserId
    ) {
        return projectGroupTodoService.toggleTodoStatus(groupId, todoId, currentUserId);
    }
}