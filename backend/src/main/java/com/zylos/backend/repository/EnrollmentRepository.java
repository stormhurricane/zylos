package com.zylos.backend.repository;

import com.zylos.backend.model.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByCourseId(Long courseId);
    List<Enrollment> findByUserId(int userId);
    Optional<Enrollment> findByCourseIdAndUserId(Long courseId, int userId);
    void deleteByCourseIdAndUserId(Long courseId, int userId);
}
