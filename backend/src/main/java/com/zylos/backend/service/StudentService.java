package com.zylos.backend.service;

import org.springframework.stereotype.Service;

import com.zylos.backend.database.Student_old;

import java.util.List;

@Service
@Deprecated(since="2024-06", forRemoval=true)
public class StudentService {




    public Student_old findeStudent(int id) {
        return null;
    }

    public Student_old findeStudentMitMatrikelnummer(int matrikelNummer){
        // Student student = studentRepository.findStudentByMatrikelnr(matrikelNummer);
        // return student;
        return null;
    }

    public List<Student_old> gibAlleStudenten() {
        // return studentRepository.findAll();
        return null;
    }

}
