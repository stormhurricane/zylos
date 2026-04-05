package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.ArbeitsThema;
import com.zylos.backend.repository.ArbeitsThemaRepository;

import java.util.List;

@Service
public class ArbeitsThemaService {

    @Autowired
    ArbeitsThemaRepository arbeitsThemaRepository;

    public boolean erstelleArbeitsThema(ArbeitsThema arbeitsThema){
        arbeitsThemaRepository.save(arbeitsThema);
        return true;
    }

    public List<ArbeitsThema> gibAlleArbeitsThemenEinesLehrenden(int lehrendenId){
        return arbeitsThemaRepository.findAllByLehrendenId(lehrendenId);
    }
}
