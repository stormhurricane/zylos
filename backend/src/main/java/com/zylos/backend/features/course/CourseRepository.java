package com.zylos.backend.features.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByTitle(String title);
    List<Course> findByTitleContainingIgnoreCase(String title);
}
