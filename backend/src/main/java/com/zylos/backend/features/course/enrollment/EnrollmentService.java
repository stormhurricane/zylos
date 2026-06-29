package com.zylos.backend.features.course.enrollment;

import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseRepository;
import com.zylos.backend.features.course.CourseUserClient;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.exceptions.CourseNotFoundException; // FIX: Import
import com.zylos.backend.features.course.enrollment.dto.CourseParticipantsResponse;
import com.zylos.backend.features.course.enrollment.exceptions.EnrollmentUserNotFoundException; // FIX: Neue Exception
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
    public void enrollUser(Long courseId, long userId) {
        if (enrollmentRepository.findByCourseIdAndUserId(courseId, userId).isPresent()) {
            return; // Bereits eingeschrieben -> Idempotent abfangen
        }
        
        // FIX: Nutzt existierende Business-Exception
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        
        // FIX: Nutzt neue dedizierte Exception
        if (!courseUserClient.existsById(userId)) {
            throw new EnrollmentUserNotFoundException(userId);
        }

        enrollmentRepository.save(new Enrollment(userId, course));
    }

    @Transactional(rollbackFor = Exception.class)
    public void unenrollUser(Long courseId, long userId) {
        enrollmentRepository.deleteByCourseIdAndUserId(courseId, userId);
    }

    @Transactional(readOnly = true) // FIX: Read-only Performance
    public CourseParticipantsResponse getCategorizedParticipants(Long courseId) {
        logger.info("Processing categorized participants for course: {}", courseId);
        
        // Checken ob Kurs überhaupt existiert, bevor wir ins Leere laufen
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

    @Transactional(readOnly = true) // FIX: Read-only Performance
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