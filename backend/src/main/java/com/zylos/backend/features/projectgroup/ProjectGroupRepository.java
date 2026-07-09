package com.zylos.backend.features.projectgroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectGroupRepository extends JpaRepository<ProjectGroup, Long> {
    Optional<ProjectGroup> findByTitle(String title);
    List<ProjectGroup> findByCourseId(Long courseId);
    List<ProjectGroup> findByTitleContainingIgnoreCase(String title);
}