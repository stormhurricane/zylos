package com.zylos.backend.controller.communication;

import com.zylos.backend.database.Lehrender;
import com.zylos.backend.database.Nutzer;
import com.zylos.backend.database.Student;

public class NutzerWrapper {

    private Student moeglicherStudent;
    private Lehrender moeglicherLehrender;

    public NutzerWrapper(Nutzer nutzer){
        if (nutzer instanceof Student) {
            this.moeglicherStudent = (Student) nutzer;
        }
        else if (nutzer instanceof Lehrender) {
            this.moeglicherLehrender = (Lehrender) nutzer;
        }
    }

    public Student getMoeglicherStudent() {
        return moeglicherStudent;
    }

    public Lehrender getMoeglicherLehrender() {
        return moeglicherLehrender;
    }
}
