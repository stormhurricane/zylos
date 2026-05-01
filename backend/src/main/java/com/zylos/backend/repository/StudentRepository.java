package com.zylos.backend.repository;

import com.zylos.backend.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {    
    Optional<Student> findByMatriculationNumber(String matriculationNumber);

    @Query("SELECT MAX(s.matriculationNumber) FROM Student s")
    Optional<String> findMaxMatriculationNumber();
}
