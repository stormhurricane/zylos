package Services;

import Controller.Communication.QuizWrapper;
import Controller.Communication.StatistikWrapper;
import Controller.Communication.VeranstaltungsWrapper;
import Controller.LehrenderEndpoint;
import Controller.NutzerEndpoint;
import datenklassen.*;
import okhttp3.RequestBody;
import retrofit2.Call;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class LehrenderService {

    private LehrenderEndpoint lehrenderClient;

    public LehrenderService(LehrenderEndpoint lehrenderClient){
        this.lehrenderClient = lehrenderClient;
    }

    public void aktualisiereLehrender(int id, Map<String, String> lehrender){
        try {
            lehrenderClient.updateProfil(id, lehrender).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean legeLehrveranstaltungAn(int sessionid, VeranstaltungsWrapper lv) {
        try {
            boolean answer = lehrenderClient.erstelleLehrveranstaltung(sessionid, lv).execute().body();
            return answer;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean legeLVAn(int sessionid, String[] neueLV) {
        String nameDerLV = neueLV[0];
        Lehrveranstaltung.typEnum artDerLV = this.bestimmeLVArt(neueLV[1]);
        String[] zeitAngabe = neueLV[2].split(" ");
        // wird Header abgefangen?
        if ( zeitAngabe.length == 2) {
            Lehrveranstaltung.zeitEnum semesterDerLV = this.bestimmeSemester(zeitAngabe[0]);
            String jahrDerLV = this.bestimmeJahr(zeitAngabe[1], semesterDerLV);
            Lehrveranstaltung ug = new Lehrveranstaltung(nameDerLV, jahrDerLV, artDerLV, semesterDerLV );
            VeranstaltungsWrapper ugg = new VeranstaltungsWrapper(ug);

            try {
                return lehrenderClient.erstelleLehrveranstaltung(sessionid, ugg).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String bestimmeJahr(String jahr, Lehrveranstaltung.zeitEnum semester) {
        if (semester == Lehrveranstaltung.zeitEnum.SS) {
            if (jahr.length() == 2 ) return "20" + jahr;
            else return jahr;
        }
        if (semester == Lehrveranstaltung.zeitEnum.WS) {
            if (jahr.length() == 5) {
                return "20" + jahr.substring(0,2) + "/20" + jahr.substring(3);
            }
            if (jahr.length() == 9) {
                if (jahr.contains("-")) { return jahr.replace("-", "/"); }
                return jahr;
            }
        }
        return null; //Sollte nicht passieren
    }

    public Lehrveranstaltung.zeitEnum bestimmeSemester(String semester) {
        if (semester.startsWith("s") || semester.startsWith("S")) {
            return Lehrveranstaltung.zeitEnum.SS;
        }
        else if (semester.startsWith("w") || semester.startsWith("W")) {
            return Lehrveranstaltung.zeitEnum.WS;
        }
        else return null;           //Sollte nicht passieren
    }

    private Lehrveranstaltung.typEnum bestimmeLVArt(String lvArt) {
        if (lvArt.trim().equalsIgnoreCase("vorlesung")) {
            return Lehrveranstaltung.typEnum.VORLESUNG;
        }
        else if (lvArt.trim().equalsIgnoreCase("seminar")) {
            return Lehrveranstaltung.typEnum.SEMINAR;
        }
        else return null;           //Sollte nicht passieren
    }

    public boolean fuegeStudentZu(Map<String, Integer> zufuegeMap){
        try {
            boolean abruf = lehrenderClient.fuegeStudentzuLVHinzu(zufuegeMap).execute().body();
            return abruf;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean fuegeLVMaterialHinzu(LehrveranstaltungsMaterial lehrveranstaltungsMaterial){
        try{
            boolean erfolg = lehrenderClient.fuegeLVMaterialHinzu(lehrveranstaltungsMaterial).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Integer> sucheStudent(Map<String, String> name){
        try {
            ArrayList<Integer> studid = lehrenderClient.sucheStudent(name).execute().body();
            return studid;
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(-2);
        return a;
    }

    //Methoden für quiz
    public boolean legeQuizAn(QuizWrapper quizWrapper){
        try {
            boolean erfolg = lehrenderClient.legeQuizAn(quizWrapper).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public StatistikWrapper zeigeStatistik(int testid){
        try {
            StatistikWrapper s = lehrenderClient.zeigeStatistikEinesTests(testid).execute().body();
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Methoden für Kalender

    public Integer erstelleTermin(Termin termin){
        try {
            Integer tid = lehrenderClient.legeTerminAn(termin).execute().body();
            return tid;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -2;
    }

    public boolean reminderTermin(Reminder r){
        try {
            boolean erfolg = lehrenderClient.reminderTermin(r).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Methode für Themenangebote
    public boolean erstelleThema(ArbeitsThema thema){
        try {
            boolean erfolg = lehrenderClient.erstelleThema(thema).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean erstelleBewertung(QuizWrapper quizWrapper){
        try {
            boolean erfolg = lehrenderClient.erstelleBewertung(quizWrapper).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
