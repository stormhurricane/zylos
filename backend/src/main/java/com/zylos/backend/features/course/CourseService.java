package com.zylos.backend.features.course;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zylos.backend.features.course.dto.CourseRequest;
import com.zylos.backend.features.course.dto.CourseResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // FIX: @Transactional entfernt, da die Hoheit beim Orchestrator liegt
    public CourseResponse createCourse(CourseRequest request) {
        if (courseRepository.findByTitle(request.title()).isPresent()) {
            // TODO: In der nächsten Session durch CourseAlreadyExistsException(409) ersetzen!
            throw new RuntimeException("Course with title '" + request.title() + "' already exists.");
        }
        Course course = new Course(request.title(), request.type(), request.term(), request.academicYear());
        Course savedCourse = courseRepository.save(course);
        
        return mapToResponse(savedCourse);
    }

    public CourseResponse getCourseById(Long id) {
        logger.info("Service: Fetching course details for ID: {}", id);
        return courseRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> {
                    logger.error("Service: Course with ID {} not found in database", id);
                    // TODO: In der nächsten Session durch CourseNotFoundException(404) ersetzen!
                    return new RuntimeException("Course not found");
                });
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CourseResponse> searchByTitle(String title) {
        return courseRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // FIX: @Transactional entfernt. Der Orchestrator steuert die atomare Klammer.
    public List<CourseResponse> importFromCsv(MultipartFile file) {
        List<CourseResponse> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String separator = line.contains(";") ? ";" : ",";
                String[] data = line.split(separator);
                
                if (data.length >= 4) {
                    String title = data[0].trim();
                    
                    // FIX: Defensiver Check vorab! Verhindert das Zerstören der Orchestrator-Transaktion.
                    if (courseRepository.findByTitle(title).isPresent()) {
                        logger.warn("CSV-Import: Course with title '{}' already exists. Skipping line.", title);
                        continue; 
                    }

                    CourseRequest request = new CourseRequest(
                        title,
                        CourseType.valueOf(data[1].trim().toUpperCase()),
                        SemesterTerm.valueOf(data[2].trim().toUpperCase()),
                        data[3].trim()
                    );
                    
                    results.add(createCourse(request));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return results;
    }

    private CourseResponse mapToResponse(Course course) {
        return new CourseResponse(
            course.getId(),
            course.getTitle(),
            course.getType(),
            course.getTerm(),
            course.getAcademicYear()
        );
    }

    boolean isDatabaseEmpty() {
        return courseRepository.count() == 0;
    }
}