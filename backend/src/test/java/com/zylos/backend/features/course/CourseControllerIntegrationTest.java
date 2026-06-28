package com.zylos.backend.features.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseRepository;
import com.zylos.backend.features.course.CourseType;
import com.zylos.backend.features.course.SemesterTerm;
import com.zylos.backend.features.course.dto.CourseRequest;
import com.zylos.backend.features.course.enrollment.EnrollmentRepository;
import com.zylos.backend.features.course.material.CourseMaterialRepository;
import com.zylos.backend.config.security.UserPrincipal; // Dein Record
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseMaterialRepository courseMaterialRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    private Long generatedTeacherId;

    @BeforeEach
    void setup() throws Exception {
        enrollmentRepository.deleteAll(); 
        courseMaterialRepository.deleteAll();

        courseRepository.deleteAll();

        Map<String, Object> teacherRequest = Map.of(
            "firstName", "Test",
            "lastName", "Instructor",
            "email", "instructor@test.com",
            "password", "password",
            "privateAddress", "Address",
            "researchArea", "Research",
            "chair", "Chair"
        );

        // 1. Lehrer registrieren
        mockMvc.perform(post("/api/users/register/teacher")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacherRequest)))
                .andExpect(status().isCreated());
        
        // 2. Echte ID aus DB holen und in Klassenvariable speichern
        generatedTeacherId = entityManager.createQuery(
                "SELECT u.id FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", "instructor@test.com")
                .getSingleResult();
    }

    // [Certain] Hilfsmethode, die den Request dynamisch authentifiziert (Best Practice!)
    private RequestPostProcessor mockUser(Long id, String email, String role) {
        UserPrincipal principal = new UserPrincipal(id, email);
        return authentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                principal, "password", java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role))
        ));
    }

    @Test
    void shouldCreateCourseAndUploadMaterial() throws Exception {
        CourseRequest request = new CourseRequest("Integration Test Course", CourseType.LECTURE, SemesterTerm.WINTER, "2024/25");
        
        // 1. Create Course - Wir hängen den mockUser direkt mit der dynamischen ID an den Request!
        String courseJson = mockMvc.perform(post("/api/courses")
                .with(mockUser(generatedTeacherId, "instructor@test.com", "INSTRUCTOR")) // <-- SAUBER!
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print()) // <-- FÜGE DIESE ZEILE EIN
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        
        Long courseId = objectMapper.readTree(courseJson).get("id").asLong();

        // 2. Upload Material
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello World".getBytes());
        
        String materialJson = mockMvc.perform(multipart("/api/courses/" + courseId + "/materials")
                .file(file)
                .with(mockUser(generatedTeacherId, "instructor@test.com", "INSTRUCTOR")) // <-- HIER AUCH!
                .param("title", "My Document"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("My Document"))
                .andReturn().getResponse().getContentAsString();
        
        Long materialId = objectMapper.readTree(materialJson).get("id").asLong();

        // 3. Download Material
        mockMvc.perform(get("/api/courses/materials/" + materialId + "/download")
                .with(mockUser(generatedTeacherId, "instructor@test.com", "INSTRUCTOR")))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.txt\""))
                .andExpect(content().bytes("Hello World".getBytes()));
    }

    @Test
    void studentShouldNotBeAbleToUploadMaterial() throws Exception {
        Course course = courseRepository.save(new Course("Restricted Course", CourseType.SEMINAR, SemesterTerm.SUMMER, "2024"));
        MockMultipartFile file = new MockMultipartFile("file", "virus.exe", "application/octet-stream", new byte[0]);

        mockMvc.perform(multipart("/api/courses/" + course.getId() + "/materials")
                .file(file)
                .with(mockUser(999L, "student@test.com", "STUDENT")) // Beliebige ID, da es an der Rolle scheitert
                .param("title", "Hack"))
                .andExpect(status().isForbidden());
    }
}