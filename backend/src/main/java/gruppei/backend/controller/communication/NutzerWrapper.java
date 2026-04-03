package gruppei.backend.controller.communication;

import gruppei.backend.database.Lehrender;
import gruppei.backend.database.Nutzer;
import gruppei.backend.database.Student;

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
