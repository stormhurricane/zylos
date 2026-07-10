package com.zylos.backend.features.projectgroup.todo;

import com.zylos.backend.features.projectgroup.ProjectGroup;
import com.zylos.backend.features.projectgroup.ProjectGroupRepository;
import com.zylos.backend.features.projectgroup.exception.ProjectGroupNotFoundException;
import com.zylos.backend.features.projectgroup.member.ProjectGroupMemberService;
import com.zylos.backend.features.projectgroup.todo.dto.TodoRequest;
import com.zylos.backend.features.projectgroup.todo.dto.TodoResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectGroupTodoService {

    private final ProjectGroupTodoRepository projectGroupTodoRepository;
    private final ProjectGroupRepository projectGroupRepository;
    private final ProjectGroupMemberService projectGroupMemberService; // Hier nutzen wir den neuen Service!

    @Transactional
    public TodoResponse createTodo(Long groupId, TodoRequest request, Long currentUserId) {
        projectGroupMemberService.verifyMemberAccess(groupId, currentUserId);

        ProjectGroup group = projectGroupRepository.findById(groupId)
                .orElseThrow(() -> new ProjectGroupNotFoundException(groupId));

        ProjectGroupTodo todo = ProjectGroupTodo.builder()
                .projectGroup(group)
                .title(request.title())
                .assignedToUserId(request.assignedToUserId())
                .build();

        return TodoResponse.fromEntity(projectGroupTodoRepository.save(todo));
    }

    @Transactional
    public TodoResponse toggleTodoStatus(Long groupId, Long todoId, Long currentUserId) {
        projectGroupMemberService.verifyMemberAccess(groupId, currentUserId);

        ProjectGroupTodo todo = projectGroupTodoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo mit ID " + todoId + " nicht gefunden."));

        if (!todo.getProjectGroup().getId().equals(groupId)) {
            throw new IllegalArgumentException("Dieses Todo gehört nicht zur angegebenen Gruppe.");
        }

        todo.setCompleted(!todo.isCompleted());
        return TodoResponse.fromEntity(todo);
    }

    @Transactional(readOnly = true)
    public List<TodoResponse> getTodos(Long groupId, Long currentUserId) {
        projectGroupMemberService.verifyMemberAccess(groupId, currentUserId);
        
        return projectGroupTodoRepository.findByProjectGroupId(groupId).stream()
                .map(TodoResponse::fromEntity)
                .toList();
    }
}