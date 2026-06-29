package com.zylos.backend.features.course.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByCourseId(Long courseId);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.course WHERE e.userId = :userId")
    List<Enrollment> findByUserId(@Param("userId") long userId);

    Optional<Enrollment> findByCourseIdAndUserId(Long courseId, long userId);

    @Modifying
    @Query("DELETE FROM Enrollment e WHERE e.course.id = :courseId AND e.userId = :userId")
    void deleteByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") long userId);
}
