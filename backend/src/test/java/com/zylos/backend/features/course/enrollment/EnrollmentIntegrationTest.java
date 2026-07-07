package com.zylos.backend.features.course.enrollment;

import com.zylos.backend.BaseIntegrationTest;
import com.zylos.backend.config.security.WithMockUserPrincipal;
import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseRepository;
import com.zylos.backend.features.course.CourseType;
import com.zylos.backend.features.course.SemesterTerm;
import com.zylos.backend.features.course.CourseUserClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EnrollmentIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManager entityManager; 

    @MockBean
    private CourseUserClient courseUserClient;

    @Test
    @Transactional
    @WithMockUserPrincipal(id = 42L, role = "STUDENT")
    void enrollCurrentUser_ShouldReturnCreated_WhenDataIsValid() throws Exception {
        entityManager.createNativeQuery(
            "INSERT INTO users (id, email, first_name, last_name, password, user_type) " +
            "VALUES (42, 'student@zylos.com', 'Hermine', 'Granger', 'secret123', 'STUDENT')"
        ).executeUpdate();

        // 2. Arrange Course
        Course course = new Course();
        course.setTitle("Advanced Software Engineering");
        course.setAcademicYear("2026");
        course.setTerm(SemesterTerm.SUMMER); 
        course.setType(CourseType.LECTURE); 
        course = courseRepository.save(course); 
        Long savedCourseId = course.getId();

        // 3. Mocks
        when(courseUserClient.existsById(anyLong())).thenReturn(true);
        when(courseUserClient.isStudent(anyLong())).thenReturn(true);

        // 4. Act & Assert
        mockMvc.perform(post("/api/courses/" + savedCourseId + "/enroll"))
               .andExpect(status().isCreated());
    }

    @Test
    @WithMockUserPrincipal(id = 42L, role = "STUDENT")
    void unenrollCurrentUser_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/courses/1/unenroll"))
               .andExpect(status().isNoContent());
    }

    @Test
    void enrollCurrentUser_ShouldReturn401_WhenUserNotAuthenticated() throws Exception {
        mockMvc.perform(post("/api/courses/1/enroll"))
               .andExpect(status().isUnauthorized());
    }
}