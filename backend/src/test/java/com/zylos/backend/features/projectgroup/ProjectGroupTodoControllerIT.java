package com.zylos.backend.features.projectgroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zylos.backend.BaseIntegrationTest;
import com.zylos.backend.config.security.UserPrincipal;
import com.zylos.backend.features.projectgroup.member.ProjectGroupRole;
import com.zylos.backend.features.projectgroup.todo.ProjectGroupTodo;
import com.zylos.backend.features.projectgroup.todo.ProjectGroupTodoRepository;
import com.zylos.backend.features.projectgroup.todo.dto.TodoRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

class ProjectGroupTodoControllerIT extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectGroupRepository projectGroupRepository;

    @Autowired
    private ProjectGroupTodoRepository projectGroupTodoRepository;

    private ProjectGroup savedGroup;
    private final Long testUserId = 42L; // Muss mit der ID aus studentAuth() übereinstimmen

    @BeforeEach
    void setUp() {
        projectGroupTodoRepository.deleteAll();
        projectGroupRepository.deleteAll();

        ProjectGroup group = ProjectGroup.builder()
                .title("Test-Architektur-Gruppe")
                .createdBy(testUserId) 
                .build();
        
        group.addMember(testUserId, ProjectGroupRole.ADMIN); 
        
        savedGroup = projectGroupRepository.save(group);
    }

    private UsernamePasswordAuthenticationToken studentAuth() {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
        UserPrincipal mockPrincipal = new UserPrincipal(testUserId, "student@zylos.de"); 
        return new UsernamePasswordAuthenticationToken(mockPrincipal, null, authorities);
    }

    @Test
    void createTodo_ShouldReturnCreated_WhenUserIsMember() throws Exception {
        TodoRequest request = new TodoRequest("Datenbank aufsetzen", testUserId);

        mockMvc.perform(post("/api/project-groups/{groupId}/todos", savedGroup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(studentAuth()))) 
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Datenbank aufsetzen")))
                .andExpect(jsonPath("$.isCompleted", is(false)));
    }

    @Test
    void createTodo_ShouldReturnForbidden_WhenUserIsNotMember() throws Exception {
        ProjectGroup otherGroup = projectGroupRepository.save(ProjectGroup.builder()
                .title("Fremde Gruppe")
                .createdBy(99L)
                .build());

        TodoRequest request = new TodoRequest("Unbefugter Task", testUserId);

        mockMvc.perform(post("/api/project-groups/{groupId}/todos", otherGroup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(studentAuth())))
                .andExpect(status().isForbidden());
    }

    @Test
    void getTodos_ShouldReturnList() throws Exception {
        ProjectGroupTodo todo = ProjectGroupTodo.builder()
                .projectGroup(savedGroup)
                .title("Doku schreiben")
                .isCompleted(false)
                .build();
        projectGroupTodoRepository.save(todo);

        mockMvc.perform(get("/api/project-groups/{groupId}/todos", savedGroup.getId())
                        .with(authentication(studentAuth())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Doku schreiben")));
    }
}