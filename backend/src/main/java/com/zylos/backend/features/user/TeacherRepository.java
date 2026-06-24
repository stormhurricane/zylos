package com.zylos.backend.features.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface TeacherRepository extends JpaRepository<Teacher, Long> {

    boolean existsByUserId(long userId);
    Optional<Teacher> findByUserId(long userId); 
}
