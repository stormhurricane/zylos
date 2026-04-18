package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.Student_old;
import com.zylos.backend.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;


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
