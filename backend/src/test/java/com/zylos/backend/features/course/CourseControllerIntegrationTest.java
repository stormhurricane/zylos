package com.zylos.backend.features.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zylos.backend.BaseIntegrationTest;
import com.zylos.backend.config.security.CourseSecurityEvaluator;
import com.zylos.backend.config.security.UserPrincipal;
import com.zylos.backend.config.security.WithMockUserPrincipal;
import com.zylos.backend.features.course.dto.CourseRequest;
import com.zylos.backend.features.course.material.CourseMaterial;
import com.zylos.backend.features.course.material.CourseMaterialRepository;
import com.zylos.backend.features.user.Teacher;
import com.zylos.backend.features.user.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
class CourseControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMaterialRepository materialRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseSecurityEvaluator courseSecurityEvaluator;

    private Long generatedTeacherId;

    @BeforeEach
    void setUpInstructor() {
        Teacher teacher = new Teacher();
        teacher.setEmail("instructor@zylos.com");
        teacher.setFirstName("Severus");
        teacher.setLastName("Snape");
        teacher.setPassword("potions123");
        
        teacher = teacherRepository.save(teacher);
        this.generatedTeacherId = teacher.getId(); 
    }

    @Test
    void shouldCreateCourseSuccessfully() throws Exception {
        var principal = new UserPrincipal(generatedTeacherId, "instructor@zylos.com");
        var auth = new UsernamePasswordAuthenticationToken(
                principal, null, java.util.List.of(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        CourseRequest request = new CourseRequest("Integration Test Course", CourseType.LECTURE, SemesterTerm.WINTER, "2024/2025");

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.title").value("Integration Test Course"));
    }

    @Test
    void shouldUploadMaterial_WhenUserIsInstructorAndHasWriteAccess() throws Exception {
        var principal = new UserPrincipal(generatedTeacherId, "instructor@zylos.com");
        var auth = new UsernamePasswordAuthenticationToken(
                principal, null, java.util.List.of(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        Course course = courseRepository.save(new Course("System Architecture", CourseType.LECTURE, SemesterTerm.SUMMER, "2026"));
        when(courseSecurityEvaluator.hasWriteAccess(eq(course.getId()), eq(generatedTeacherId))).thenReturn(true);

        MockMultipartFile file = new MockMultipartFile("file", "slides.pdf", MediaType.APPLICATION_PDF_VALUE, "content".getBytes());

        mockMvc.perform(multipart("/api/courses/" + course.getId() + "/materials")
                .file(file)
                .param("title", "Lecture 1 Slides"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("Lecture 1 Slides"))
               .andExpect(jsonPath("$.fileName").value("slides.pdf"));
    }

    @Test
    @WithMockUserPrincipal(id = 999L, role = "STUDENT")
    void studentShouldNotBeAbleToUploadMaterial() throws Exception {
        Course course = courseRepository.save(new Course("Restricted Course", CourseType.SEMINAR, SemesterTerm.SUMMER, "2024"));
        MockMultipartFile file = new MockMultipartFile("file", "virus.exe", "application/octet-stream", new byte[0]);

        mockMvc.perform(multipart("/api/courses/" + course.getId() + "/materials")
                .file(file)
                .param("title", "Hack"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUserPrincipal(id = 888L, role = "INSTRUCTOR")
    void foreignTeacherShouldNotBeAbleToUploadMaterial() throws Exception {
        Course course = courseRepository.save(new Course("Teacher A Course", CourseType.LECTURE, SemesterTerm.WINTER, "2024"));
        when(courseSecurityEvaluator.hasWriteAccess(eq(course.getId()), eq(888L))).thenReturn(false);

        MockMultipartFile file = new MockMultipartFile("file", "lecture.pdf", "application/pdf", "Content".getBytes());

        mockMvc.perform(multipart("/api/courses/" + course.getId() + "/materials")
                .file(file)
                .param("title", "Sabotage"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUserPrincipal(id = 777L, role = "STUDENT")
    void downloadMaterial_ShouldReturnFileResource_WhenUserHasReadAccess() throws Exception {
        Course course = courseRepository.save(new Course("Security Blueprint", CourseType.LECTURE, SemesterTerm.SUMMER, "2026"));
        byte[] fileContent = "secret-blueprint-bytes".getBytes();
        CourseMaterial material = materialRepository.save(new CourseMaterial("Blueprint", "blueprint.pdf", MediaType.APPLICATION_PDF_VALUE, fileContent, course));

        when(courseSecurityEvaluator.hasReadAccess(eq(course.getId()), eq(777L))).thenReturn(true);

        mockMvc.perform(get("/api/courses/materials/" + material.getId() + "/download"))
               .andExpect(status().isOk())
               .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, containsString("blueprint.pdf")))
               .andExpect(content().bytes(fileContent));
    }

    @Test
    void uploadMaterial_WithNonExistentCourse_ShouldReturnNotFound() throws Exception {
        var principal = new UserPrincipal(generatedTeacherId, "instructor@zylos.com");
        var auth = new UsernamePasswordAuthenticationToken(
                principal, null, java.util.List.of(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        MockMultipartFile file = new MockMultipartFile("file", "doc.pdf", "application/pdf", "Content".getBytes());

        mockMvc.perform(multipart("/api/courses/99999/materials")
                .file(file)
                .param("title", "Ghost Doc"))
               .andExpect(status().isNotFound());
    }
}