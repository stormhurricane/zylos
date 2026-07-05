package com.zylos.backend.features.course;

import com.zylos.backend.features.course.dto.CourseRequest;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.staff.CourseStaffService;
import com.zylos.backend.features.course.staff.StaffRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseCreationOrchestrator {

    private final CourseService courseService;
    private final CourseStaffService courseStaffService;

    @Transactional(rollbackFor = Exception.class)
    public CourseResponse createCourseWithInstructor(CourseRequest request, long instructorId) {
        CourseResponse course = courseService.createCourse(request);
        courseStaffService.addStaff(course.id(), instructorId, StaffRole.OWNER);        
        return course;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<CourseResponse> importFromCsvWithInstructor(MultipartFile file, long instructorId) {
        List<CourseResponse> importedCourses = courseService.importFromCsv(file);
        
        for (CourseResponse course : importedCourses) {
            courseStaffService.addStaff(course.id(), instructorId, StaffRole.OWNER);
        }
        
        return importedCourses;
    }
}