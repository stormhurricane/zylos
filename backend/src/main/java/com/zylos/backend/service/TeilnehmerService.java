package com.zylos.backend.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.zylos.backend.controller.communication.NutzerWrapper;
import com.zylos.backend.controller.communication.VeranstaltungsWrapper;
import com.zylos.backend.database.*;
import com.zylos.backend.repository.TeilnehmerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeilnehmerService {

    private final LehrveranstaltungsService lehrveranstaltungsService;
    private final NutzerService nutzerService;
    private final TeilnehmerRepository teilnehmerRepository;
    private final ReminderService reminderService;

    public TeilnehmerService(
            @Lazy LehrveranstaltungsService lehrveranstaltungsService,
            NutzerService nutzerService,
            TeilnehmerRepository teilnehmerRepository,
            @Lazy ReminderService reminderService) {
        this.lehrveranstaltungsService = lehrveranstaltungsService;
        this.nutzerService = nutzerService;
        this.teilnehmerRepository = teilnehmerRepository;
        this.reminderService = reminderService;
    }

    public List<VeranstaltungsWrapper> erstelleTeilnahmeListeEinesNutzers(int nutzerId) {
        List<Teilnehmer> teilnehmerliste = teilnehmerRepository.findAll();
        List<Teilnehmer> LvListeDesTeilnehmers = new ArrayList<>();

        for (Teilnehmer t : teilnehmerliste) {
            if (t.getId().getNutzer_id()==nutzerId) {
                LvListeDesTeilnehmers.add(t);
            }
        }

        List<VeranstaltungsWrapper> lehrveranstaltungList = lehrveranstaltungsService.erzeugeLehrveranstaltungsListe();
        List<VeranstaltungsWrapper> LvObjekteDesTeilnehmers = new ArrayList<>();
        for (Teilnehmer t : LvListeDesTeilnehmers) {
            for (VeranstaltungsWrapper v : lehrveranstaltungList) {
                Lehrveranstaltung lv;
                if (v.getProjektgruppe() != null) {
                    lv = v.getProjektgruppe();
                } else {
                    lv = v.getLehrveranstaltung();
                }

                if (t.getId().getLehrveranstaltungsID()==lv.getLehrveranstaltungsID()) {
                    LvObjekteDesTeilnehmers.add(v);
                }
            }
        }
        return LvObjekteDesTeilnehmers;
    }

    public List<NutzerWrapper> erstelleTeilnehmerListeEinerLV(int lvID) {
        List<Teilnehmer> teilnehmerliste = teilnehmerRepository.findAll();
        List<Teilnehmer> teilnehmerDerLV = new ArrayList<>();

        for (Teilnehmer t : teilnehmerliste) {
            if (t.getId().getLehrveranstaltungsID()==lvID) {
                teilnehmerDerLV.add(t);
            }
        }
        //NutzerWrapper-Liste, damit FrontEnd zwischen Lehrenden & Studenten differenzieren kann
        List<NutzerWrapper> teilnahmerWrapper = new ArrayList<>();
        for (Nutzer teilnehmer : nutzerService.findeAlleNutzerVonLV(teilnehmerDerLV)) {
            teilnahmerWrapper.add(new NutzerWrapper(teilnehmer));
        }
        return teilnahmerWrapper;
    }


    public boolean fuegeTeilnehmerHinzu(int nutzerId, int lvId) {
        List<Teilnehmer> teilnehmerListe = teilnehmerRepository.findAll();
        for(Teilnehmer t: teilnehmerListe){
            if ((t.getId().getNutzer_id() == nutzerId) && (t.getId().getLehrveranstaltungsID() == lvId) ){
                return false;
            }
        }
        ListID addedParticipant = new ListID(nutzerId, lvId);
        Teilnehmer teilnehmer = new Teilnehmer(addedParticipant);
        teilnehmerRepository.save(teilnehmer);
        reminderService.fuegeNachzueglerHinzu(nutzerId, lvId);
        return true;
    }

    public boolean pruefeTeilnahme(int nutzerId, int projektgruppenId) {
        Optional<Teilnehmer> bisherigeTeilnahme = teilnehmerRepository.findById(new ListID(nutzerId, projektgruppenId));
        if (bisherigeTeilnahme.isPresent()) {
            return true;
        }
        else {return false;}
    }

    public List<Student> erstelleStudentenListeEinerLV(int lvId) {
        List<NutzerWrapper> teilnehmerListe = this.erstelleTeilnehmerListeEinerLV(lvId);
        List<Student> studenten = new ArrayList<>();

        for (NutzerWrapper teilnehmer : teilnehmerListe) {
            if (teilnehmer.getMoeglicherStudent() != null) {
                studenten.add(teilnehmer.getMoeglicherStudent());
            }
        }

        return studenten;

    }

    public boolean pruefeGemeinsameLehrveranstaltung(int studentenId, int lehrendenId){
        List<VeranstaltungsWrapper> alleLVdesStudenten = this.erstelleTeilnahmeListeEinesNutzers(studentenId);
        List<VeranstaltungsWrapper> alleLVdesLehrenden = this.erstelleTeilnahmeListeEinesNutzers(lehrendenId);

        for(VeranstaltungsWrapper studentenVeranstaltung: alleLVdesStudenten){
            if(studentenVeranstaltung.getLehrveranstaltung() == null){
                continue;
            }
            int studentenLvId = studentenVeranstaltung.getLehrveranstaltung().getLehrveranstaltungsID();

            for(VeranstaltungsWrapper lehrendenVeranstaltungen: alleLVdesLehrenden){
                if(lehrendenVeranstaltungen.getLehrveranstaltung() == null){
                    continue;
                }
                if(studentenLvId == lehrendenVeranstaltungen.getLehrveranstaltung().getLehrveranstaltungsID()){
                    return true;
                }
            }
        }
        return false;
    }
}
