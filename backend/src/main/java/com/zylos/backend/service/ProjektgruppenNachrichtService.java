package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.ProjektgruppenNachricht;
import com.zylos.backend.repository.ProjektgruppenNachrichtRepository;

import java.util.Comparator;
import java.util.List;

@Service
public class ProjektgruppenNachrichtService {

    @Autowired
    ProjektgruppenNachrichtRepository projektgruppenNachrichtRepository;

    public List<ProjektgruppenNachricht> zeigeAlleNachrichtenEinerProjektugruppe(int projektgruppenId){
        List<ProjektgruppenNachricht> projektgruppenNachrichtList = projektgruppenNachrichtRepository.findAllByProjektgruppenId(projektgruppenId);
        projektgruppenNachrichtList.sort(new Comparator<ProjektgruppenNachricht>() {
            @Override
            public int compare(ProjektgruppenNachricht o1, ProjektgruppenNachricht o2) {
                if (o1.getId() < o2.getId()) {return -1;}
                else if (o1.getId() > o2.getId()) {return 1;}
                else {return 0;}
            }
        });
        return projektgruppenNachrichtList;
    }

    public List<ProjektgruppenNachricht> fuegeNachrichtHinzu(int projektgruppenId, String sender, String inhalt) {
        projektgruppenNachrichtRepository.save(new ProjektgruppenNachricht(projektgruppenId, sender, inhalt));
        return this.zeigeAlleNachrichtenEinerProjektugruppe(projektgruppenId);
    }
}
