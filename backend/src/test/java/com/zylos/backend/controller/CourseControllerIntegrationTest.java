package com.zylos.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zylos.backend.model.dto.CourseRequest;
import com.zylos.backend.model.entity.Course;
import com.zylos.backend.model.entity.CourseType;
import com.zylos.backend.model.entity.SemesterTerm;
import com.zylos.backend.model.entity.Teacher;
import com.zylos.backend.repository.CourseRepository;
import com.zylos.backend.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    void setup() {
        courseRepository.deleteAll();
        teacherRepository.deleteAll();

        // Erstelle einen Lehrenden, der als "aktueller Nutzer" im Test fungiert
        Teacher teacher = new Teacher("Test", "Instructor", "instructor@test.com", "Address", "password", null, "Research", "Chair");
        teacherRepository.save(teacher);
    }

    @Test
    @WithMockUser(username = "instructor@test.com", roles = "INSTRUCTOR")
    void shouldCreateCourseAndUploadMaterial() throws Exception {
        // 1. Create Course
        CourseRequest request = new CourseRequest("Integration Test Course", CourseType.LECTURE, SemesterTerm.WINTER, "2024/25");
        
        String courseJson = mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        
        Long courseId = objectMapper.readTree(courseJson).get("id").asLong();

        // 2. Upload Material
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello World".getBytes());
        
        String materialJson = mockMvc.perform(multipart("/api/courses/" + courseId + "/materials")
                .file(file)
                .param("title", "My Document"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("My Document"))
                .andReturn().getResponse().getContentAsString();
        
        Long materialId = objectMapper.readTree(materialJson).get("id").asLong();

        // 3. Download Material
        mockMvc.perform(get("/api/courses/materials/" + materialId + "/download"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.txt\""))
                .andExpect(content().bytes("Hello World".getBytes()));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void studentShouldNotBeAbleToUploadMaterial() throws Exception {
        Course course = courseRepository.save(new Course("Restricted Course", CourseType.SEMINAR, SemesterTerm.SUMMER, "2024"));
        MockMultipartFile file = new MockMultipartFile("file", "virus.exe", "application/octet-stream", new byte[0]);

        mockMvc.perform(multipart("/api/courses/" + course.getId() + "/materials")
                .file(file)
                .param("title", "Hack"))
                .andExpect(status().isForbidden());
    }
}
