package com.zylos.backend.features.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByMatriculationNumber(String matriculationNumber);
    
    @Query("SELECT MAX(s.matriculationNumber) FROM Student s")
    Optional<String> findMaxMatriculationNumber();
}
