package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.controller.communication.VeranstaltungsWrapper;
import com.zylos.backend.database.Termin;
import com.zylos.backend.repository.TerminRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class TerminService {

    @Autowired
    TerminRepository terminRepository;

    @Autowired
    TeilnehmerService teilnehmerService;

    //wir unterstellen dass die Kombi aus lvId, zeitpunkt und betreff unique ist
    public int legeTerminAn(Termin termin) {
        Termin termin1 = terminRepository.save(termin);
        return termin1.getId();
    }

    public List<Termin> terminListeEinesNutzers (int nutzerId, Map<String, Integer> dateMap) {
        List<VeranstaltungsWrapper> veranstaltungsWrappers = teilnehmerService.erstelleTeilnahmeListeEinesNutzers(nutzerId);
        List<Integer> lvIds = new ArrayList<>();
        for(VeranstaltungsWrapper veranstaltungsWrapper : veranstaltungsWrappers) {
            if(veranstaltungsWrapper.getLehrveranstaltung()!=null) {
                lvIds.add(veranstaltungsWrapper.getLehrveranstaltung().getLehrveranstaltungsID());
            }
            else {
                lvIds.add(veranstaltungsWrapper.getProjektgruppe().getLehrveranstaltungsID());
            }
        }
        List<Termin> terminList = new ArrayList<>();
        for (int lvId : lvIds) {
            terminList.addAll(terminRepository.findAllByLvIdAndJahrAndMonatAndTag(lvId, dateMap.get("jahr").toString(), dateMap.get("monat").toString(), dateMap.get("tag").toString()));
        }
        terminList.sort(new Comparator<Termin>() {
            @Override
            public int compare(Termin o1, Termin o2) {

                LocalTime t1 = LocalTime.parse(o1.getUhrzeit());
                LocalTime t2 = LocalTime.parse(o2.getUhrzeit());

                return t1.compareTo(t2);
            }
        });
        return terminList;
    }

    public Termin findeTerminMitId(int id){
       return terminRepository.findById(id);
    }

    public List<Termin> findeTermineEinerLv(int lvId) {
        return terminRepository.findAllByLvId(lvId);
    }
}
