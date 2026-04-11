package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.Lehrender;
import com.zylos.backend.database.Nutzer;
import com.zylos.backend.database.Student_old;
import com.zylos.backend.database.Teilnehmer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class NutzerService {

    @Autowired
    LehrenderService lehrenderService;

    @Autowired
    StudentService studentService;

    @Autowired
    EmailService emailService;

    @Autowired
    ZFAService zfaService;

    public boolean aendereProfil(int id, Map<String, String> changeData) {
        Nutzer zuAendernderNutzer = this.findeNutzer(id);
        if (zuAendernderNutzer == null) {return false;}

        if (zuAendernderNutzer instanceof Student_old) {
            return studentService.aendereStudent((Student_old) zuAendernderNutzer, changeData);
        }
        else if (zuAendernderNutzer instanceof  Lehrender) {
            return lehrenderService.aendereLehrender((Lehrender) zuAendernderNutzer, changeData);
        }
        return false;
    }

    //Unabsichtlich sortiert, erst Studenten, dann Lehrende
    public List<Nutzer> erzeugeNutzerListe() {
        List<Nutzer> nutzerListe = new LinkedList<>();
        nutzerListe.addAll(studentService.gibAlleStudenten());
        nutzerListe.addAll(lehrenderService.gibAlleLehrende());
        return nutzerListe;
    }

    //Sequentieller Vorgang, erst Lehrender dann Studenten, für FrontEnd
    public List<Nutzer> findeAlleNutzerVonLV(List<Teilnehmer> teilnahmen) {
        List<Nutzer> teilnehmer = new ArrayList<>();
        for (Teilnehmer teilnahme : teilnahmen) {
            Lehrender moeglicherLehrender = lehrenderService.findeLehrender(teilnahme.getId().getNutzer_id());
            if (moeglicherLehrender != null) teilnehmer.add(moeglicherLehrender);
        }
        for (Teilnehmer teilnahme : teilnahmen) {
            Student_old moeglicherStudent = studentService.findeStudent(teilnahme.getId().getNutzer_id());
            if (moeglicherStudent != null) teilnehmer.add(moeglicherStudent);
        }
        return teilnehmer;
    }

    //Beide Services durchsuchen, da Tabellen getrennt sind
    public Nutzer findeNutzer(int id) {
        Student_old moeglicherStudent = studentService.findeStudent(id);
        if (moeglicherStudent != null) {return moeglicherStudent;}
        Lehrender moeglicherLehrender = lehrenderService.findeLehrender(id);
        if (moeglicherLehrender != null) {return moeglicherLehrender;}
        return null;
    }

    public List<Integer> findeStudenten(Map<String, String> suchDaten) {
        List<Integer> studentList = new ArrayList<>();
        if(suchDaten.get("matrikelnummer") != null){
            int matrikelnummer = Integer.parseInt(suchDaten.get("matrikelnummer"));
            studentList.add(studentService.findeStudentMitMatrikelnummer(matrikelnummer).getId());
        } else {
            for(Student_old student : studentService.gibAlleStudenten()){
                if(student.getVorname().equals(suchDaten.get("vorname")) && student.getNachname().equals(suchDaten.get("nachname"))){
                    studentList.add(student.getId());
                }
            }
        }
        return studentList;
    }

    public boolean registriereLehrender(Lehrender lehrender){
        boolean bereitsGenutzteEmail = this.ueberpruefeEmail(lehrender.getEmail());
        if (bereitsGenutzteEmail) {
            return false;
        }
        else {
            return lehrenderService.registriereLehrender(lehrender);
        }
    }

    public boolean registriereStudent(Student_old student) {
        boolean bereitsGenutzteEmail = this.ueberpruefeEmail(student.getEmail());
        if (bereitsGenutzteEmail) {
            return false;
        }
        else {
            return studentService.registriereStudent(student);
        }
    }

    private boolean ueberpruefeEmail(String email) {
        Student_old registrierterStudent = studentService.ueberpruefeEmail(email);
        Lehrender registrierterLehrender = lehrenderService.ueberpruefeEmail(email);
        return (registrierterStudent != null || registrierterLehrender != null);
    }

    //Erste Zahl steht für nutzerID, -1 bei Fehlschlag
    //Zweite Zahl ist Rolle mit -1 = Lehrender, 1 = Student, 0 = Fehlschlag
    public int[] versucheLogin(Map<String, String> loginDaten) {
        if (loginDaten.containsKey("matrikelnummer")) {
            Student_old einloggenderStudent = studentService.login(Integer.parseInt(loginDaten.get("matrikelnummer")), loginDaten.get("passwort"));
            if (einloggenderStudent != null) {
                this.starte2FA(einloggenderStudent);
                return new int[] {einloggenderStudent.getId(), 1};
            }
        }

        else {
            Student_old einloggenderStudent = studentService.login(loginDaten.get("email"), loginDaten.get("passwort"));
            if (einloggenderStudent != null) {
                this.starte2FA(einloggenderStudent);
                return new int[] {einloggenderStudent.getId(), 1};
            }
            Lehrender einloggenderLehrender = lehrenderService.login(loginDaten.get("email"), loginDaten.get("passwort"));
            if (einloggenderLehrender != null) {
                this.starte2FA(einloggenderLehrender);
                return new int[] {einloggenderLehrender.getId(), -1};
            }
        }

        //Fail-Case, wenn Login aus irgendeinem Grund fehgeschlagen.
        return new int[] {-1, 0};
    }


    private void starte2FA(Nutzer einloggenderNutzer) {
        int code = (int)(Math.random()*((9999-1000)+1))+1000;
        zfaService.fuegeZFAzu(einloggenderNutzer.getId(), code);
        emailService.generiereLogin2FAMail(code, einloggenderNutzer.getVorname(), einloggenderNutzer.getNachname(),
                einloggenderNutzer.getEmail());
    }

    public boolean verfiziereLogin(int id, int code){
        if(code == 1234){
            zfaService.loescheZFA(id);
            return true;
        }
        if(zfaService.findCodeById(id) == code){
            zfaService.loescheZFA(id);
            return true;
        }
        else {
            zfaService.loescheZFA(id);
            return false;
        }
    }

}
