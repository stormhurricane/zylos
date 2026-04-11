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

    //Anhand der vorhandenen, vereinbarten Keys werden mögliche Änderungen durchgeführt
    public boolean aendereStudent(Student_old student, Map<String, String> changeData) {
        if (changeData.isEmpty()) return true;
        if (changeData.containsKey("adresse")) {
            student.setAdresse(changeData.get("adresse"));
        }
        if (changeData.containsKey("profilbild")) {
            student.setProfilbild(changeData.get("profilbild"));
        }
        if (changeData.containsKey("passwort")) {
            student.setPasswort(changeData.get("passwort"));
        }
        if (changeData.containsKey("studienfach")) {
            student.setStudienfach(changeData.get("studienfach"));
        }

        // studentRepository.save(student);
        return true;
    }

    public Student_old findeStudent(int id) {
        // Optional<Student> student = studentRepository.findById(id);
        // if (student.isPresent()) {
        //     return student.get();
        // }
        // else { return null;}
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


    public Student_old login(int matrikelnummer, String passwort) {
        // Student einloggenderStudent = studentRepository.findStudentByMatrikelnr(matrikelnummer);
        // if (einloggenderStudent != null) {
        //     if (einloggenderStudent.getPasswort().equals(passwort)) {
        //         return einloggenderStudent;
        //     }
        // }
        return null;
    }

    public Student_old login(String email, String passwort) {
        // Student einloggenderStudent = studentRepository.findStudentByEmail(email);
        // if (einloggenderStudent != null) {
        //     if (einloggenderStudent.getPasswort().equals(passwort)) {
        //         return einloggenderStudent;
        //     }
        // }
        return null;
    }

    public boolean registriereStudent(Student_old student) {
            // student.setMatrikelnr(this.generiereMatrikelNr());
            // studentRepository.save(student);
            return true;
    }

    public Student_old ueberpruefeEmail(String email) {
        // return studentRepository.findStudentByEmail(email);
        return null;
    }

}
