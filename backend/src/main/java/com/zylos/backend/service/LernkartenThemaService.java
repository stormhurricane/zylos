package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.LernkartenThema;
import com.zylos.backend.repository.LernkartenThemaRepository;

import java.util.List;

@Service
@Deprecated(since="2024-06", forRemoval=true)
public class LernkartenThemaService {

    @Autowired
    LernkartenThemaRepository lernkartenThemaRepository;

    //Beschreibung innerhalb einer lvId muss unique sein
    public int LernkartenThemaErstellen(int lvId, String beschreibung) {
        LernkartenThema lernkartenThema = lernkartenThemaRepository.findByLvIdAndBeschreibung(lvId, beschreibung);
        if (lernkartenThema == null) {
            return (lernkartenThemaRepository.save(new LernkartenThema(beschreibung, lvId))).getId();
        }
        return -1;
    }

    public List<LernkartenThema> ListeAllerLernkartenThemen(int lvId) {
        return lernkartenThemaRepository.findAllByLvId(lvId);
    }
}
