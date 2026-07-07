package com.zylos.backend.controller.communication;

import com.zylos.backend.database.Lehrender;
import com.zylos.backend.database.Nutzer;
import com.zylos.backend.database.Student_old;

@Deprecated(since = "2026-04", forRemoval = true)
public class NutzerWrapper {

    private Student_old moeglicherStudent;
    private Lehrender moeglicherLehrender;

    public NutzerWrapper(Nutzer nutzer){
        if (nutzer instanceof Student_old) {
            this.moeglicherStudent = (Student_old) nutzer;
        }
        else if (nutzer instanceof Lehrender) {
            this.moeglicherLehrender = (Lehrender) nutzer;
        }
    }

    public Student_old getMoeglicherStudent() {
        return moeglicherStudent;
    }

    public Lehrender getMoeglicherLehrender() {
        return moeglicherLehrender;
    }
}
