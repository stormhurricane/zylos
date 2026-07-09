package com.zylos.backend.features.projectgroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zylos.backend.BaseIntegrationTest;
import com.zylos.backend.config.security.UserPrincipal;
import com.zylos.backend.features.course.*;
import com.zylos.backend.features.projectgroup.dto.ProjectGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectGroupIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProjectGroupRepository projectGroupRepository;

    private Course testCourse;

    @BeforeEach
    void setUp() {
        projectGroupRepository.deleteAll();
        courseRepository.deleteAll();

        testCourse = new Course("Software Engineering 101", CourseType.LECTURE, SemesterTerm.SUMMER, "2026");
        testCourse = courseRepository.save(testCourse);
    }

    private UsernamePasswordAuthenticationToken studentAuth() {
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
        
        UserPrincipal mockPrincipal = new UserPrincipal(42L, "student@zylos.de"); 
        
        return new UsernamePasswordAuthenticationToken(mockPrincipal, null, authorities);
    }

    @Test
    void student_should_successfully_create_project_group() throws Exception {
        ProjectGroupRequest request = new ProjectGroupRequest("Lerngruppe Architektur " + System.currentTimeMillis(), testCourse.getId());

        mockMvc.perform(post("/api/project-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(studentAuth())))
                        .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    void should_return_409_when_title_already_exists() throws Exception {
        String duplicateTitle = "Unique Group Name";
        
        ProjectGroup existing = ProjectGroup.builder().title(duplicateTitle).createdBy(1L).build();
        projectGroupRepository.saveAndFlush(existing);

        ProjectGroupRequest duplicateRequest = new ProjectGroupRequest(duplicateTitle, null);

        mockMvc.perform(post("/api/project-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest))
                        .with(authentication(studentAuth())))
                .andExpect(status().isConflict()); 
    }

    @Test
    void student_cannot_add_members_manually() throws Exception {
        mockMvc.perform(post("/api/project-groups/1/members")
                        .param("userId", "99")
                        .with(authentication(studentAuth())))
                .andExpect(status().isForbidden()); 
    }
}