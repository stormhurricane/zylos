package com.zylos.backend.features.course;

import com.zylos.backend.features.course.dto.CourseRequest;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.dto.UserCoursesSummaryResponse;
import com.zylos.backend.features.course.enrollment.EnrollmentService;
import com.zylos.backend.features.course.enrollment.dto.CourseParticipantsResponse;
import com.zylos.backend.features.course.exceptions.CourseAlreadyExistsException;
import com.zylos.backend.features.course.exceptions.CourseNotFoundException;
import com.zylos.backend.features.course.staff.CourseStaffService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor 
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentService enrollmentService;
    private final CourseStaffService courseStaffService;
    private final CourseUserClient courseUserClient;
    private final Validator validator;

    @Transactional(rollbackFor = Exception.class)
    public CourseResponse createCourse(CourseRequest request) {
        if (courseRepository.findByTitle(request.title()).isPresent()) {
            throw new CourseAlreadyExistsException(request.title());
        }
        Course course = new Course(request.title(), request.type(), request.term(), request.academicYear());
        Course savedCourse = courseRepository.save(course);
        
        return new CourseResponse(savedCourse);
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        log.info("Service: Fetching course details for ID: {}", id);
        return courseRepository.findById(id)
                .map(CourseResponse::new) 
                .orElseThrow(() -> {
                    log.error("Service: Course with ID {} not found in database", id);
                    return new CourseNotFoundException(id);
                });
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
            return courseRepository.findAllWithEnrollments().stream()
                .map(CourseResponse::new) 
                .toList(); 
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> searchByTitle(String title) {
        return courseRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(CourseResponse::new) 
                .toList(); 
    }

    @Transactional(rollbackFor = Exception.class) 
    public List<CourseResponse> importFromCsv(MultipartFile file) {
        List<CourseResponse> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String separator = line.contains(";") ? ";" : ",";
                String[] data = line.split(separator);
                
                if (data.length >= 4) {
                    String title = data[0].trim();
                    
                    if (courseRepository.findByTitle(title).isPresent()) {
                        log.warn("CSV-Import: Course with title '{}' already exists. Skipping line.", title);
                        continue; 
                    }

                    try {
                        CourseType type = CourseType.valueOf(data[1].trim().toUpperCase());
                        SemesterTerm term = SemesterTerm.valueOf(data[2].trim().toUpperCase());
                        String academicYear = data[3].trim();

                        CourseRequest request = new CourseRequest(title, type, term, academicYear);
                        
                        Set<ConstraintViolation<CourseRequest>> violations = validator.validate(request);
                        if (!violations.isEmpty()) {
                            log.error("CSV-Import: Validation failed for line '{}': {}", line, violations.iterator().next().getMessage());
                            continue;
                        }

                        results.add(createCourse(request));
                    } catch (IllegalArgumentException e) {
                        log.error("CSV-Import: Invalid Enum value in line: '{}'. Skipping.", line);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return results;
    }

    @Transactional(readOnly = true)
    public CourseParticipantsResponse getCategorizedParticipants(Long courseId) {
        validateCourseExists(courseId);

        List<Long> staffIds = courseStaffService.getStaffIdsForCourse(courseId);
        List<Long> studentIds = enrollmentService.getStudentIdsForCourse(courseId);

        List<Long> allUserIds = new ArrayList<>();
        allUserIds.addAll(staffIds);
        allUserIds.addAll(studentIds);

        return courseUserClient.categorizeUsersByIds(allUserIds);
    }

    @Transactional(readOnly = true)
    public UserCoursesSummaryResponse getMyCoursesSummary(long userId) {
        List<CourseResponse> teaching = courseStaffService.getCoursesForStaff(userId);
        List<CourseResponse> enrolled = enrollmentService.getEnrolledCourses(userId);

        return new UserCoursesSummaryResponse(teaching, enrolled);
    }

    public void validateCourseExists(Long courseId) {
    if (!courseRepository.existsById(courseId)) {
        throw new CourseNotFoundException(courseId);
    }
}

}