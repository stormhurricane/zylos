package com.zylos.backend.features.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface TeacherRepository extends JpaRepository<Teacher, Long> {

}
