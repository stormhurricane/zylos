package com.zylos.backend.features.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByCourseId(Long courseId);
    List<Enrollment> findByUserId(long userId);
    Optional<Enrollment> findByCourseIdAndUserId(Long courseId, long userId);
    void deleteByCourseIdAndUserId(Long courseId, long userId);
}
