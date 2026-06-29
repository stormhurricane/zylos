package com.zylos.backend.features.course.enrollment;

import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseRepository;
import com.zylos.backend.features.course.CourseUserClient;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.exceptions.CourseNotFoundException;
import com.zylos.backend.features.course.enrollment.dto.CourseParticipantsResponse;
import com.zylos.backend.features.course.enrollment.exceptions.EnrollmentUserNotFoundException;
import com.zylos.backend.features.course.enrollment.exceptions.InvalidRoleForEnrollmentException; // NEU
import com.zylos.backend.features.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);
    
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final CourseUserClient courseUserClient;

    @Transactional(rollbackFor = Exception.class)
    public void addEnrollment(Long courseId, long userId, EnrollmentRole role) {
        if (enrollmentRepository.findByCourseIdAndUserId(courseId, userId).isPresent()) {
            return;
        }
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        
        if (!courseUserClient.existsById(userId)) {
            throw new EnrollmentUserNotFoundException(userId);
        }

        // Typsichere Rollen-Validierung gegen den Client
        if (role == EnrollmentRole.STUDENT && !courseUserClient.isStudent(userId)) {
            throw new InvalidRoleForEnrollmentException(userId, "STUDENT");
        } else if (role == EnrollmentRole.INSTRUCTOR && !courseUserClient.isInstructor(userId)) {
            throw new InvalidRoleForEnrollmentException(userId, "INSTRUCTOR");
        }

        // FIX: Übergibt die Rolle an den korrigierten Entity-Konstruktor
        enrollmentRepository.save(new Enrollment(userId, course, role));
    }


    @Transactional(rollbackFor = Exception.class)
    public void unenrollUser(Long courseId, long userId) {
        enrollmentRepository.deleteByCourseIdAndUserId(courseId, userId);
    }

    @Transactional(readOnly = true)
    public CourseParticipantsResponse getCategorizedParticipants(Long courseId) {
        logger.info("Processing categorized participants for course: {}", courseId);
        
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException(courseId);
        }

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        
        List<Long> userIds = enrollments.stream()
                .map(Enrollment::getUserId)
                .collect(Collectors.toList());

        Map<String, List<UserResponse>> categorized = courseUserClient.categorizeUsersByIds(userIds);

        return new CourseParticipantsResponse(
                categorized.getOrDefault("instructors", List.of()),
                categorized.getOrDefault("students", List.of())
        );
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getEnrolledCourses(long userId) {
        return enrollmentRepository.findByUserId(userId).stream()
                .map(e -> new CourseResponse(e.getCourse())) 
                .collect(Collectors.toList());
    }

}