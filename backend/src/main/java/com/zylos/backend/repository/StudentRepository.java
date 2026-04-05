package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findStudentByEmail(String email);

    Student findStudentByMatrikelnr(int matrikelnr);

    Student findStudentByVornameAndNachname(String vorname, String nachname);

}
