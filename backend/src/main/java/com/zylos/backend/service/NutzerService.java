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
@Deprecated(since="2024-06", forRemoval=true)
public class NutzerService {

    @Autowired
    LehrenderService lehrenderService;

    @Autowired
    StudentService studentService;

    @Autowired
    EmailService emailService;

    @Autowired
    ZFAService zfaService;

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

    //Erste Zahl steht für nutzerID, -1 bei Fehlschlag
    //Zweite Zahl ist Rolle mit -1 = Lehrender, 1 = Student, 0 = Fehlschlag
    @SuppressWarnings("unused")
    public int[] versucheLogin(Map<String, String> loginDaten) {
        if (loginDaten.containsKey("matrikelnummer")) {
            Student_old einloggenderStudent = null;
            if (einloggenderStudent != null) {
                this.starte2FA(einloggenderStudent);
                return new int[] {einloggenderStudent.getId(), 1};
            }
        }

        else {
            Student_old einloggenderStudent = null;
            if (einloggenderStudent != null) {
                this.starte2FA(einloggenderStudent);
                return new int[] {einloggenderStudent.getId(), 1};
            }
            Lehrender einloggenderLehrender = null;
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
