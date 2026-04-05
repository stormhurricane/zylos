package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.Lernkarte;
import com.zylos.backend.repository.LernkartenRepository;

import java.util.List;

@Service
public class LernkartenService {

    @Autowired
    LernkartenRepository lernkartenRepository;

    public boolean LernkarteErstellen(Lernkarte lernkarte) {
        lernkartenRepository.save(lernkarte);
        return true;
    }

    public List<Lernkarte> ListeLernkartenEinesThemas(int lernkartenThemaId) {
        return lernkartenRepository.findAllByLernkartenThemaId(lernkartenThemaId);
    }

}
