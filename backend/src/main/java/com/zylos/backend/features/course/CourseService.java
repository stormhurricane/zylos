package com.zylos.backend.features.course;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zylos.backend.features.course.dto.CourseRequest;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.exceptions.CourseAlreadyExistsException;
import com.zylos.backend.features.course.exceptions.CourseNotFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository courseRepository;
    private final Validator validator; // FIX: Ermöglicht manuelle DTO-Validierung im Service

    public CourseService(CourseRepository courseRepository, Validator validator) {
        this.courseRepository = courseRepository;
        this.validator = validator;
    }

    @Transactional(rollbackFor = Exception.class)
    public CourseResponse createCourse(CourseRequest request) {
        if (courseRepository.findByTitle(request.title()).isPresent()) {
            throw new CourseAlreadyExistsException(request.title());
        }
        Course course = new Course(request.title(), request.type(), request.term(), request.academicYear());
        Course savedCourse = courseRepository.save(course);
        
        return mapToResponse(savedCourse);
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        logger.info("Service: Fetching course details for ID: {}", id);
        return courseRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> {
                    logger.error("Service: Course with ID {} not found in database", id);
                    return new CourseNotFoundException(id);
                });
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
            return courseRepository.findAllWithEnrollments().stream()
                .map(this::mapToResponse)
                .toList(); // FIX: Modernisiert
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> searchByTitle(String title) {
        return courseRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToResponse)
                .toList(); // FIX: Modernisiert
    }

    @Transactional(rollbackFor = Exception.class) // FIX: Import schlägt ganz fehl oder gar nicht (Atomarität)
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
                        logger.warn("CSV-Import: Course with title '{}' already exists. Skipping line.", title);
                        continue; 
                    }

                    try {
                        CourseType type = CourseType.valueOf(data[1].trim().toUpperCase());
                        SemesterTerm term = SemesterTerm.valueOf(data[2].trim().toUpperCase());
                        String academicYear = data[3].trim();

                        CourseRequest request = new CourseRequest(title, type, term, academicYear);
                        
                        // FIX: Manuelle Validierung triggern, damit unsaubere Regex-Formate (z.B. "2024/25") blockiert werden!
                        Set<ConstraintViolation<CourseRequest>> violations = validator.validate(request);
                        if (!violations.isEmpty()) {
                            logger.error("CSV-Import: Validation failed for line '{}': {}", line, violations.iterator().next().getMessage());
                            continue;
                        }

                        results.add(createCourse(request));
                    } catch (IllegalArgumentException e) {
                        logger.error("CSV-Import: Invalid Enum value in line: '{}'. Skipping.", line);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return results;
    }

    private CourseResponse mapToResponse(Course course) {
        // FIX: @Transactional(readOnly = true) an den Lesemethoden fängt den Lazy-Initialization-Fehler hier ab,
        // sofern die Verknüpfung in der Course-Entity korrekt gemappt ist.
        return new CourseResponse(
            course.getId(),
            course.getTitle(),
            course.getType(),
            course.getTerm(),
            course.getAcademicYear(),
            course.getEnrollments() != null ? course.getEnrollments().size() : 0
        );
    }
}