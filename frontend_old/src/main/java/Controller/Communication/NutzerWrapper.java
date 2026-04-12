package Controller.Communication;

import datenklassen.Lehrender;
import datenklassen.Nutzer;
import datenklassen.Student;

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
        // What else?
    }

    public Student getMoeglicherStudent() {
        return moeglicherStudent;
    }

    public Lehrender getMoeglicherLehrender() {
        return moeglicherLehrender;
    }
}