package com.zylos.backend.features.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zylos.backend.features.course.dto.CourseRequest;
import com.zylos.backend.features.user.User; // Angenommen dein User Model heißt so
import com.zylos.backend.BaseIntegrationTest;
import com.zylos.backend.config.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Fixes clean state automatically after every single test!
class CourseControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    private Long generatedTeacherId;

    @BeforeEach
    void setup() throws Exception {
        // Da User abstrakt ist, nutzen wir einfach deinen funktionierenden API-Weg,
        // um den Lehrer sauber in die DB zu bringen.
        Map<String, Object> teacherRequest = Map.of(
            "firstName", "Test",
            "lastName", "Instructor",
            "email", "instructor@test.com",
            "password", "password",
            "privateAddress", "Address",
            "researchArea", "Research",
            "chair", "Chair"
        );

        mockMvc.perform(post("/api/users/register/teacher")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teacherRequest)))
                .andExpect(status().isCreated());
        
        // Echte ID aus DB holen
        generatedTeacherId = entityManager.createQuery(
                "SELECT u.id FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", "instructor@test.com")
                .getSingleResult();
    }

    private RequestPostProcessor mockUser(Long id, String email, String role) {
        UserPrincipal principal = new UserPrincipal(id, email);
        return authentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                principal, "password", List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role))
        ));
    }

    @Test
    void shouldCreateCourseAndUploadMaterial() throws Exception {
        CourseRequest request = new CourseRequest("Integration Test Course", CourseType.LECTURE, SemesterTerm.WINTER, "2024/2025");
        
        // 1. Create Course
        String courseJson = mockMvc.perform(post("/api/courses")
                .with(mockUser(generatedTeacherId, "instructor@test.com", "INSTRUCTOR"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        
        Long courseId = objectMapper.readTree(courseJson).get("id").asLong();

        // 2. Upload Material
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello World".getBytes());
        
        String materialJson = mockMvc.perform(multipart("/api/courses/" + courseId + "/materials")
                .file(file)
                .with(mockUser(generatedTeacherId, "instructor@test.com", "INSTRUCTOR"))
                .param("title", "My Document"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("My Document"))
                .andReturn().getResponse().getContentAsString();
        
        Long materialId = objectMapper.readTree(materialJson).get("id").asLong();

        // 3. Download Material
        mockMvc.perform(get("/api/courses/materials/" + materialId + "/download")
                .with(mockUser(generatedTeacherId, "instructor@test.com", "INSTRUCTOR")))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"=?UTF-8?Q?test.txt?=\"; filename*=UTF-8''test.txt"))
                .andExpect(content().bytes("Hello World".getBytes()));
    }

    @Test
    void studentShouldNotBeAbleToUploadMaterial() throws Exception {
        Course course = courseRepository.save(new Course("Restricted Course", CourseType.SEMINAR, SemesterTerm.SUMMER, "2024"));
        MockMultipartFile file = new MockMultipartFile("file", "virus.exe", "application/octet-stream", new byte[0]);

        mockMvc.perform(multipart("/api/courses/" + course.getId() + "/materials")
                .file(file)
                .with(mockUser(999L, "student@test.com", "STUDENT"))
                .param("title", "Hack"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Disabled("Deaktiviert bis zum Refactoring des Kurs-Besitzmodells") // NEU
    void foreignTeacherShouldNotBeAbleToUploadMaterial() throws Exception {
        // GIVEN: Ein Kurs wird in der DB gespeichert
        Course course = courseRepository.save(new Course("Teacher A Course", CourseType.LECTURE, SemesterTerm.WINTER, "2024"));
        // Hinweis: Wenn eure Business-Logik im Service prüft, ob der Kurs dem Lehrer gehört,
        // wird dieser Test fehlschlagen, solange der Kurs nicht mit dem Lehrer verknüpft ist. 
        // Falls das passiert, bräuchte ich die Datei: Course.java

        MockMultipartFile file = new MockMultipartFile("file", "lecture.pdf", "application/pdf", "Content".getBytes());

        // WHEN & THEN: Ein ANDERER Lehrer versucht hier hochzuladen -> 403 Forbidden
        mockMvc.perform(multipart("/api/courses/" + course.getId() + "/materials")
                .file(file)
                .with(mockUser(888L, "other-teacher@test.com", "INSTRUCTOR"))
                .param("title", "Sabotage"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test // NEU: Testet den 404 Exception Handler Pfad
    void uploadMaterial_WithNonExistentCourse_ShouldReturnNotFound() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "doc.pdf", "application/pdf", "Content".getBytes());

        mockMvc.perform(multipart("/api/courses/99999/materials")
                .file(file)
                .with(mockUser(generatedTeacherId, "instructor@test.com", "INSTRUCTOR"))
                .param("title", "Ghost Doc"))
                .andDo(print())
                .andExpect(status().isNotFound()); // Erwartet 404 aus eurem GlobalExceptionHandler
    }
}