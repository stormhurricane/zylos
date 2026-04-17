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

    // TODO: VeranstaltungsWrapper durch CourseRequest ersetzen
    @PostMapping(path="/create/{id}")
    public boolean erstelleLehrveranstaltung(@PathVariable("id") int lehrenderID,
                                            @RequestBody VeranstaltungsWrapper lehrveranstaltung) {
        return lehrveranstaltungsService.erstelleLehrveranstaltung(lehrenderID, lehrveranstaltung);
    }

    // TODO: Rückgabetyp auf CourseResponse ändern
    @GetMapping(path="/find/{id}", produces = "application/json")
    public VeranstaltungsWrapper findeLehrveranstaltungMitID(@PathVariable("id") int id) {
        return lehrveranstaltungsService.findeLehrveranstaltung(id);
    }

    // TODO: Map<String, String> durch CourseSearchRequest ersetzen, Rückgabe CourseResponse
    @PostMapping(path="/find", consumes = "application/json", produces = "application/json")
    public VeranstaltungsWrapper findeLehrveranstaltungMitTitel(@RequestBody CourseSearchRequest suchDaten) {
        return lehrveranstaltungsService.findeLehrveranstaltung(suchDaten);
    }

    // TODO: Rückgabetyp auf List<CourseResponse> ändern
    @GetMapping(path= "/all")
    public List<VeranstaltungsWrapper> zeigeAlleLehrveranstaltungen(){
        return lehrveranstaltungsService.erzeugeLehrveranstaltungsListe();
    }

    // --- TODO: Auslagern in ProjektgruppeController ---

    @GetMapping(path="/chat/{id}")
    public List<ProjektgruppenNachricht> zeigeGruppenChat(@PathVariable("id") int pgId){
        return projektgruppeService.zeigeGruppenchat(pgId);
    }

    // --- TODO: Auslagern in ProjektgruppeController ---
    @PostMapping(path=("/chat/post"))
    public List<ProjektgruppenNachricht> sendeNachricht(@RequestBody ProjektgruppenNachricht nachricht){
        return projektgruppeService.sendeNachricht(nachricht);
    }

    // --- TODO: Auslagern in ProjektgruppeController ---
    @GetMapping(path="/todo/{id}")
    public List<ToDos> zeigeToDoListe(@PathVariable("id") int pgId){
        return projektgruppeService.zeigeToDoListe(pgId);
    }

    // --- TODO: Auslagern in ProjektgruppeController ---
    @PostMapping(path="/todo/add")
    public boolean neuesToDo(@RequestBody ToDos neuesToDo) {
        return projektgruppeService.fuegeToDoHinzu(neuesToDo);
    }


    // --- TODO: Auslagern in ProjektgruppeController ---
    @PutMapping(path="/todo/done")
    public boolean hakeToDoAb(@RequestBody int id){
        return projektgruppeService.hakeToDoAb(id);
    }

    // --- TODO: Auslagern in ProjektgruppeController ---
    @PutMapping(path="/todo/change/{id}")
    public boolean aendereVerantwortlichen(@PathVariable("id") int todoId,
                                           @RequestBody int verantwortlichenId){
        return projektgruppeService.aendereVerantwortung(todoId, verantwortlichenId);
    }

    // --- TODO: Auslagern in LernkartenController ---
    @PostMapping(path="/lernkartenThema/{id}")
    public int lernkartenThemaErstellen(@PathVariable("id") int lvId,
                                        @RequestBody String beschreibung){
        return lernkartenThemaService.LernkartenThemaErstellen(lvId, beschreibung);
    }

    // --- TODO: Auslagern in LernkartenController ---
    @GetMapping(path="/lernkartenThemaListe/{id}")
    public List<LernkartenThema> zeigeLernkartenThemaListe(@PathVariable("id") int lvId){
        return lernkartenThemaService.ListeAllerLernkartenThemen(lvId);
    }

    // --- TODO: Auslagern in LernkartenController ---
    @PostMapping(path="/lernkarte")
    public boolean erstelleLernkarte(@RequestBody Lernkarte lernkarte) {
        return lernkartenService.LernkarteErstellen(lernkarte);
    }

    // --- TODO: Auslagern in LernkartenController ---
    @GetMapping(path="/lernkarten/{id}")
    public List<Lernkarte> zeigeLernkartenEinesThemas(@PathVariable("id") int lernkartenThemaId) {
        return lernkartenService.ListeLernkartenEinesThemas(lernkartenThemaId);
    }

}
