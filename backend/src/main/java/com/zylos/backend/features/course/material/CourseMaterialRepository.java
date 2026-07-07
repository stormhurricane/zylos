package com.zylos.backend.features.course.material;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zylos.backend.features.course.material.dto.MaterialResponse;

import java.util.List;

@Repository
public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Long> {
    List<CourseMaterial> findByCourseId(Long courseId);

    @Query("SELECT new com.zylos.backend.features.course.material.dto.MaterialResponse(" +
           "m.id, m.title, m.fileName, m.contentType, m.fileSize, m.createdAt) " +
           "FROM CourseMaterial m WHERE m.course.id = :courseId")
    List<MaterialResponse> findAllProjectedByCourseId(@Param("courseId") Long courseId);
}
