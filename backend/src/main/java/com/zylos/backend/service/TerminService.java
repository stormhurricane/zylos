package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.controller.communication.VeranstaltungsWrapper;
import com.zylos.backend.model.dto.CreateAppointmentRequest;
import com.zylos.backend.model.dto.TerminResponse;
import com.zylos.backend.database.Termin;
import com.zylos.backend.repository.TerminRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@Deprecated(since="2024-06", forRemoval=true)
public class TerminService {

    @Autowired
    TerminRepository terminRepository;

    @Autowired
    TeilnehmerService teilnehmerService;

    //wir unterstellen dass die Kombi aus lvId, zeitpunkt und betreff unique ist
    public int legeTerminAn(CreateAppointmentRequest request) {
        Termin termin = new Termin(request.courseId(), request.year(), request.month(), request.day(), request.time(), request.subject());
        Termin termin1 = terminRepository.save(termin);
        return termin1.getId();
    }

    public List<TerminResponse> terminListeEinesNutzers (int nutzerId, Map<String, Integer> dateMap) {
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
        
        return terminList.stream()
            .map(t -> new TerminResponse(t.getId(), t.getlvId(), t.getJahr(), t.getMonat(), t.getTag(), t.getUhrzeit(), t.getBetreff()))
            .toList();
    }

    public TerminResponse findeTerminMitId(int id){
       Termin t = terminRepository.findById(id);
       if (t == null) return null;
       return new TerminResponse(t.getId(), t.getlvId(), t.getJahr(), t.getMonat(), t.getTag(), t.getUhrzeit(), t.getBetreff());
    }

    public List<Termin> findeTermineEinerLv(int lvId) {
        return terminRepository.findAllByLvId(lvId);
    }
}
