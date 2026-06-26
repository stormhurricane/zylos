package com.zylos.backend.features.course.enrollment;

import com.zylos.backend.features.course.CourseUserClient;
import com.zylos.backend.features.course.core.Course;
import com.zylos.backend.features.course.core.CourseRepository;
import com.zylos.backend.features.course.core.dto.CourseResponse;
import com.zylos.backend.features.course.enrollment.dto.CourseParticipantsResponse;
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
    private final CourseUserClient courseUserClient; // Unser neuer Schutzwall zum User-Feature

    @Transactional
    public void enrollUser(Long courseId, long userId) {
        if (enrollmentRepository.findByCourseIdAndUserId(courseId, userId).isPresent()) {
            return;
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        // Validation by the interface
        if (!courseUserClient.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        enrollmentRepository.save(new Enrollment(userId, course));
    }

    @Transactional
    public void unenrollUser(Long courseId, long userId) {
        enrollmentRepository.deleteByCourseIdAndUserId(courseId, userId);
    }

    public CourseParticipantsResponse getCategorizedParticipants(Long courseId) {
        logger.info("Processing categorized participants for course: {}", courseId);
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        
        // 1: Get user ids from enrollments
        List<Long> userIds = enrollments.stream()
                .map(Enrollment::getUserId)
                .collect(Collectors.toList());

        // Get Users by the interface
        Map<String, List<UserResponse>> categorized = courseUserClient.categorizeUsersByIds(userIds);

        return new CourseParticipantsResponse(
                categorized.getOrDefault("instructors", List.of()),
                categorized.getOrDefault("students", List.of())
        );
    }

    public List<CourseResponse> getEnrolledCourses(long userId) {
        return enrollmentRepository.findByUserId(userId).stream()
                .map(e -> mapToCourseResponse(e.getCourse()))
                .collect(Collectors.toList());
    }

    private CourseResponse mapToCourseResponse(Course course) {
        return new CourseResponse(
            course.getId(),
            course.getTitle(),
            course.getType(),
            course.getTerm(),
            course.getAcademicYear()
        );
    }
}