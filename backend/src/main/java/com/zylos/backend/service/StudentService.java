package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.Student_old;
import com.zylos.backend.repository.StudentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    private int generiereMatrikelNr() {
        int freieMatrNr = 1000000;
        // List<Student> studentenListe = studentRepository.findAll();
        List<Student_old> studentenListe = null;

        studentenListe.sort(new Comparator<Student_old>() {
            @Override
            public int compare(Student_old o1, Student_old o2) {
                if (o1.getMatrikelnr() < o2.getMatrikelnr()) {return -1;}
                else if (o1.getMatrikelnr() > o2.getMatrikelnr()) { return 1;}
                else {return 0;}
            }
        });

        for (Student_old student : studentenListe ) {
            if (freieMatrNr == student.getMatrikelnr()) {
                freieMatrNr++;
            }
        }
        return freieMatrNr;
    }

    public List<Student_old> gibAlleStudenten() {
        // return studentRepository.findAll();
        return null;
    }

}
