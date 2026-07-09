package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.*;

import java.util.List;

@Service
@Deprecated(since="2024-06", forRemoval=true)
public class ProjektgruppeService {


    @Autowired
    ProjektgruppenNachrichtService projektgruppenNachrichtService;

    @Autowired
    ToDosService toDosService;


    public List<ProjektgruppenNachricht> zeigeGruppenchat(int pgId) {
        return projektgruppenNachrichtService.zeigeAlleNachrichtenEinerProjektugruppe(pgId);
    }

    public List<ProjektgruppenNachricht> sendeNachricht(ProjektgruppenNachricht nachricht) {
        return projektgruppenNachrichtService.fuegeNachrichtHinzu(nachricht.getProjektgruppenId(),
                nachricht.getSender(), nachricht.getInhalt());
    }


    public List<ToDos> zeigeToDoListe(int pgId) {
        return toDosService.listeAllerTodosEinerProjektgruppe(pgId);
    }

    public boolean fuegeToDoHinzu(ToDos neuesToDo) {
        return toDosService.fuegeTodoHinzu(neuesToDo.getProjektgruppenId(), neuesToDo.getInhalt(),
                neuesToDo.getVerantwortlichenId());
    }

    public boolean hakeToDoAb(int id) {
        return toDosService.TodoErledigt(id);
    }

    public boolean aendereVerantwortung(int todoId, int verantwortlichenId) {
        return toDosService.aenderVerantwortlichen(todoId, verantwortlichenId);
    }
}
