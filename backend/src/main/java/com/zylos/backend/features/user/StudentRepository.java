package com.zylos.backend.features.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByMatriculationNumber(Long matriculationNumber);
    
    @Query(value = "SELECT NEXTVAL('student_matriculation_seq')", nativeQuery = true)
    Long getNextMatriculationNumber();
}
