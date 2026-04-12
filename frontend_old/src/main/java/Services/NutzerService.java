package Services;

import Controller.Communication.ChatWrapper;
import Controller.Communication.FreundschaftsAnfrage;
import Controller.Communication.NutzerWrapper;
import Controller.Communication.VeranstaltungsWrapper;
import Controller.NutzerEndpoint;
import datenklassen.*;
import retrofit2.http.Body;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NutzerService {
    private NutzerEndpoint nutzerClient;

    public NutzerService(NutzerEndpoint nutzerClient){
        this.nutzerClient = nutzerClient;
    }


    public boolean registriereStudent(Map<String, String> studentData) {
        String vorname = studentData.get("vorname");
        String nachname = studentData.get("nachname");
        String adresse = studentData.get("adresse");
        String email = studentData.get("email");
        String passwort = studentData.get("passwort");
        String studienfach = studentData.get("studienfach");
        String profilbild = studentData.get("profilbild");
        Student StudentToRegister = new Student(vorname, nachname, email,
                adresse, passwort, profilbild, studienfach);
        try {
            boolean answer = nutzerClient.profilErstellenStudent(StudentToRegister).execute().body();
            return answer;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }

    public boolean registriereLehrenden(Map<String, String> lehrendenData) {
        String vorname = lehrendenData.get("vorname");
        String nachname = lehrendenData.get("nachname");
        String adresse = lehrendenData.get("adresse");
        String email = lehrendenData.get("email");
        String passwort = lehrendenData.get("passwort");
        String lehstuhl = lehrendenData.get("lehrstuhl");
        String forschungsgebiet = lehrendenData.get("forschungsgebiet");
        String profilbild = lehrendenData.get("profilbild");
        Lehrender LehrenderToRegister = new Lehrender(vorname, nachname, email, adresse, passwort,
                                            profilbild, lehstuhl, forschungsgebiet);

        try {
            boolean answer = nutzerClient.profilErstellenLehrender(LehrenderToRegister).execute().body();
            return answer;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Nutzer rufeNutzer(int id){
        Nutzer abruf = null;
        try {
            NutzerWrapper searchedUser = nutzerClient.profilAufrufen(id).execute().body();
            if (searchedUser.getMoeglicherStudent() != null) abruf = searchedUser.getMoeglicherStudent();
            else if (searchedUser.getMoeglicherLehrender() != null) abruf = searchedUser.getMoeglicherLehrender();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return abruf;
    }
    public ArrayList<VeranstaltungsWrapper> lvListe(){
        try {
            ArrayList<VeranstaltungsWrapper> lvliste = nutzerClient.ladeLVListe().execute().body();
            return lvliste;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public VeranstaltungsWrapper sucheLVPerMap(Map<String, String> suchMap) {
        try{
            VeranstaltungsWrapper erfolg = nutzerClient.sucheLV(suchMap).execute().body();
            return erfolg;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public VeranstaltungsWrapper sucheLVPerID(int lvid) {
        try{
            VeranstaltungsWrapper erfolg = nutzerClient.sucheLVPerID(lvid).execute().body();
            return erfolg;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<LehrveranstaltungsMaterial> ladeLVMaterial(int lvid) {
        try{
            ArrayList<LehrveranstaltungsMaterial> lvMListe = nutzerClient.ladeLVMaterial(lvid).execute().body();
            return lvMListe;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean treteLVBei(Map<String, Integer> beitrittsMap){
        try {
        boolean erfolg = nutzerClient.treteVeranstaltungBei(beitrittsMap).execute().body();
        return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<NutzerWrapper> ladeLVTeilnehmer(int lvid){
        try {
            ArrayList<NutzerWrapper> teilnehmer = nutzerClient.ladeTeilnehmerliste(lvid).execute().body();
            return teilnehmer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public ArrayList<VeranstaltungsWrapper> ladeMeineLVs(int nutzerid){
        try {
            ArrayList<VeranstaltungsWrapper> lvs = nutzerClient.ladeMeineLVs(nutzerid).execute().body();
            return lvs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public boolean pruefeTeilnahme(int nutzerid, int pgid){
        try {
            boolean erfolg = nutzerClient.pruefeTeilnahme(nutzerid, pgid).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Servicemethoden für frens

    public ArrayList<NutzerWrapper> meineFreunde(int nutzerid){
        try {
            ArrayList<NutzerWrapper> freunde = nutzerClient.meineFreunde(nutzerid).execute().body();
            return freunde;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public boolean sendeAnfrage(int nutzerid, int anfrageid) {
        try {
            boolean erfolg = nutzerClient.sendeAnfrage(nutzerid, anfrageid).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean behandleAnfrage(FreundschaftsAnfrage anfrage) {
        try {
            boolean erfolg = nutzerClient.behandleAnfrage(anfrage).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<NutzerWrapper> offeneAnfragen(int nutzerid){
        try {
            ArrayList<NutzerWrapper> freunde = nutzerClient.offeneAnfragen(nutzerid).execute().body();
            return freunde;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public List<ChatNachricht> sendePN(ChatNachricht nachricht) {
        try {
            List<ChatNachricht> nachrichten = nutzerClient.sendePN(nachricht).execute().body();
            return nachrichten;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ChatWrapper ladeNachrichten(int nutzerid, int fremdid){
        try {
            ChatWrapper nachrichten = nutzerClient.ladeNachrichten(nutzerid, fremdid).execute().body();
            return nachrichten;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public ArrayList<Chat> ladeChat(int nutzerid){
        try {
            ArrayList<Chat> chats= nutzerClient.ladeMeineChats(nutzerid).execute().body();
            return chats;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public ArrayList<ProjektgruppenNachricht> zeigeGruppenChat(int pgid){
        try {
            ArrayList<ProjektgruppenNachricht> nachrichten = nutzerClient.zeigeGruppenChat(pgid).execute().body();
            return nachrichten;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public ArrayList<ProjektgruppenNachricht> sendePGNachricht(ProjektgruppenNachricht nachricht){
        try {
            ArrayList<ProjektgruppenNachricht> nachrichten = nutzerClient.sendePGNachricht(nachricht).execute().body();
            return nachrichten;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public ArrayList<ToDos> zeigeToDos(int pgid){
        try {
            ArrayList<ToDos> toDos = nutzerClient.zeigeToDos(pgid).execute().body();
            return toDos;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public boolean fuegeToDosZu(ToDos todo) {
        try {
            boolean erfolg = nutzerClient.fuegeToDosZu(todo).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hakeToDoAb(int todoid) {
        try {
            boolean erfolg = nutzerClient.hakeToDoAb(todoid).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Test> zeigeAlleQuizEinerLV(int lvid){
        try {
            ArrayList<Test> quiz = nutzerClient.zeigeAlleQuizEinerLV(lvid).execute().body();
            return quiz;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public ArrayList<Termin> ladeTermine(int nutzerid, LocalDate datum){

        Map<String, Integer> dateMap = new HashMap();
        dateMap.put("jahr", datum.getYear());
        dateMap.put("monat", datum.getMonthValue());
        dateMap.put("tag", datum.getDayOfMonth());

        try {
            ArrayList<Termin> termine = nutzerClient.ladeTermine(nutzerid, dateMap).execute().body();
            return termine;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Reminder> ladeReminder(int nutzerid, Map<String, String> dateTimeMap){
        try {
            ArrayList<Reminder> geladeneReminder = nutzerClient.ladeReminder(nutzerid, dateTimeMap).execute().body();
            return geladeneReminder;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Termin terminDesReminders(int terminid){
        try {
            Termin t = nutzerClient.terminDesReminders(terminid).execute().body();
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Methoden für lks

    public int lernkartenThemaErstellen(int lvid, @Body String thema){
        try {
            int themenid = nutzerClient.lernkartenThemaErstellen(lvid, thema).execute().body();
            return themenid;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -2;
    }

    public ArrayList<LernkartenThema> lernkartenThemaListe(int lvid){
        try {
            ArrayList<LernkartenThema> lkThemen = nutzerClient.lernkartenThemaListe(lvid).execute().body();
            return lkThemen;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean lernkarteErstellen(Lernkarte lk){
        try {
            boolean erfolg = nutzerClient.lernkarteErstellen(lk).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Lernkarte> lernkartenListe(int lktid){
        try {
            ArrayList<Lernkarte> lernkarten = nutzerClient.lernkartenListe(lktid).execute().body();
            return lernkarten;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Methoden für Themenangebote

    public ArrayList<ArbeitsThema> zeigeArbeitsThemen(int lehrendenid){
        try {
            ArrayList<ArbeitsThema> themen = nutzerClient.zeigeArbeitsThemen(lehrendenid).execute().body();
            return themen;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean gemeinsameLV(int studentid, int lehrerid){
        try {
            boolean erfolg = nutzerClient.gemeinsameLV(studentid, lehrerid).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Methoden für Bewertung
    public ArrayList<Frage> ladeBewertung(int lvid){
        try {
            ArrayList<Frage> fragen = nutzerClient.ladeBewertung(lvid).execute().body();
            return fragen;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int erstelleBewertungsversuch(int nutzerid, int lvid){
        try {
            int versuch = nutzerClient.erstelleBewertungsversuch(nutzerid, lvid).execute().body();
            return versuch;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -2;
    }

    public boolean erstelleFeedbackFuerVersuch(List<BewertungsFeedback> bewertungsFeedbacks){
        try {
            boolean erfolg = nutzerClient.erstelleFeedbackFuerVersuch(bewertungsFeedbacks).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Integer[]> erstelleBewertungsStatistik(int testid, int bestanden){
        try {
            ArrayList<Integer[]> i = nutzerClient.erstelleBewertungsStatistik(testid, bestanden).execute().body();
            return i;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean erzwingeBestehenspruefung(Map<String, String> semesterDaten){
        try {
            boolean erfolg = nutzerClient.erzwingeBestehenspruefung(semesterDaten).execute().body();
            return erfolg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
