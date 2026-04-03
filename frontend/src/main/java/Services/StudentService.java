package Services;

import Controller.NutzerEndpoint;
import Controller.StudentEndpoint;
import datenklassen.Feedback;
import datenklassen.Frage;
import datenklassen.Nutzer;
import datenklassen.Student;
import retrofit2.Call;
import retrofit2.http.Body;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentService {
    private StudentEndpoint studentClient;

    public StudentService(StudentEndpoint studentClient){
        this.studentClient = studentClient;
    }

    public Integer[] loginPerMatrikelnummer(Map<String, String> loginMap) {
        Integer[] loginid;
        try {
            loginid = studentClient.loginPerMatrikelNummer(loginMap).execute().body();
            return loginid;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loginid = new Integer[]{-2, 0};
    }

    public Integer[] loginPerEmail(Map<String, String> loginMap) {
        Integer[] loginid;
        try {
            loginid = studentClient.loginPerEmail(loginMap).execute().body();
            return loginid;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loginid = new Integer[]{-2, 0};
    }

    public Boolean verifiziereLogin(int id, int code){
        try {
            boolean erfolg = studentClient.verifiziereLogin(id, code).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void aktualisiereStudent(int id, Map<String, String> student){
        try {
            studentClient.updateProfil(id, student).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Frage> zeigeFragenEinesTests(int id){
        try {
            ArrayList<Frage> fragen = studentClient.zeigeFragenEinesTests(id).execute().body();
            return fragen;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findeFragePerId(int frageid){
        try {
            String frage = studentClient.findeFrageMitId(frageid).execute().body().getFrage();
            return frage;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }

    public Boolean legeFeedbackAn(Feedback feedback){
        try {
            boolean erfolg = studentClient.legeFeedbackAn(feedback).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Feedback> zeigeFeedbackEinesVersuchs(int id){
        try {
            ArrayList<Feedback> feedbacks = studentClient.zeigeFeedbackEinesVersuchs(id).execute().body();
            return feedbacks;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int legeVersuchAn(Map <String, Integer> versuch){
        try {
            int erfolg = studentClient.legeVersuchAn(versuch).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Boolean pruefeVersuch(int versuchsid){
        try {
            boolean erfolg = studentClient.pruefeVersuch(versuchsid).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean pruefeBearbeitung(int studentenid, int lvid){
        try {
            boolean erfolg = studentClient.pruefeBearbeitung(studentenid, lvid).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
