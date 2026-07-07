package com.zylos.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.controller.communication.VeranstaltungsWrapper;
import com.zylos.backend.database.Lernkarte;
import com.zylos.backend.database.LernkartenThema;
import com.zylos.backend.database.ProjektgruppenNachricht;
import com.zylos.backend.database.ToDos;
import com.zylos.backend.model.dto.CourseSearchRequest;
import com.zylos.backend.service.LehrveranstaltungsService;
import com.zylos.backend.service.LernkartenService;
import com.zylos.backend.service.LernkartenThemaService;
import com.zylos.backend.service.ProjektgruppeService;

import java.util.List;

@Deprecated(since = "2024-06", forRemoval = true)
@RestController
@RequestMapping(path="/api/v1/lehrveranstaltung")
public class LehrveranstaltungsController {

    @Autowired
    LehrveranstaltungsService lehrveranstaltungsService;

    @Autowired
    ProjektgruppeService projektgruppeService;

    @Autowired
    LernkartenThemaService lernkartenThemaService;

    @Autowired
    LernkartenService lernkartenService;

    @PostMapping(path="/find", consumes = "application/json", produces = "application/json")
    public VeranstaltungsWrapper findeLehrveranstaltungMitTitel(@RequestBody CourseSearchRequest suchDaten) {
        return lehrveranstaltungsService.findeLehrveranstaltung(suchDaten);
    }


    @GetMapping(path="/chat/{id}")
    public List<ProjektgruppenNachricht> zeigeGruppenChat(@PathVariable("id") int pgId){
        return projektgruppeService.zeigeGruppenchat(pgId);
    }

    @PostMapping(path=("/chat/post"))
    public List<ProjektgruppenNachricht> sendeNachricht(@RequestBody ProjektgruppenNachricht nachricht){
        return projektgruppeService.sendeNachricht(nachricht);
    }

    @GetMapping(path="/todo/{id}")
    public List<ToDos> zeigeToDoListe(@PathVariable("id") int pgId){
        return projektgruppeService.zeigeToDoListe(pgId);
    }

    @PostMapping(path="/todo/add")
    public boolean neuesToDo(@RequestBody ToDos neuesToDo) {
        return projektgruppeService.fuegeToDoHinzu(neuesToDo);
    }


    @PutMapping(path="/todo/done")
    public boolean hakeToDoAb(@RequestBody int id){
        return projektgruppeService.hakeToDoAb(id);
    }

    @PutMapping(path="/todo/change/{id}")
    public boolean aendereVerantwortlichen(@PathVariable("id") int todoId,
                                           @RequestBody int verantwortlichenId){
        return projektgruppeService.aendereVerantwortung(todoId, verantwortlichenId);
    }

    @PostMapping(path="/lernkartenThema/{id}")
    public int lernkartenThemaErstellen(@PathVariable("id") int lvId,
                                        @RequestBody String beschreibung){
        return lernkartenThemaService.LernkartenThemaErstellen(lvId, beschreibung);
    }

    @GetMapping(path="/lernkartenThemaListe/{id}")
    public List<LernkartenThema> zeigeLernkartenThemaListe(@PathVariable("id") int lvId){
        return lernkartenThemaService.ListeAllerLernkartenThemen(lvId);
    }

    @PostMapping(path="/lernkarte")
    public boolean erstelleLernkarte(@RequestBody Lernkarte lernkarte) {
        return lernkartenService.LernkarteErstellen(lernkarte);
    }

    @GetMapping(path="/lernkarten/{id}")
    public List<Lernkarte> zeigeLernkartenEinesThemas(@PathVariable("id") int lernkartenThemaId) {
        return lernkartenService.ListeLernkartenEinesThemas(lernkartenThemaId);
    }

}
