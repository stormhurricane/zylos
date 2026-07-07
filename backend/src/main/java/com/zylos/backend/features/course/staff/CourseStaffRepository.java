package com.zylos.backend.features.course.staff;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CourseStaffRepository extends JpaRepository<CourseStaff, Long> {
    Optional<CourseStaff> findByCourseIdAndUserId(Long courseId, long userId);

    List<CourseStaff> findByCourseId(Long courseId);
    
    List<CourseStaff> findByUserId(long userId);
    
    void deleteByCourseIdAndUserId(Long courseId, long userId);
    
    boolean existsByCourseIdAndUserId(Long courseId, Long userId);
}