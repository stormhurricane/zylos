package com.zylos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.controller.communication.NutzerWrapper;
import com.zylos.backend.controller.communication.VeranstaltungsWrapper;
import com.zylos.backend.model.dto.TeilnahmeRequest;
import com.zylos.backend.service.TeilnehmerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="api/v1/teilnehmerliste")
@Deprecated(since = "2026-04", forRemoval = true)
public class TeilnehmerController {

    // Aufteilung: Teils für Erzeugen und Löschen von Teilnehmern, Abfragen in den Nutzer oder VeranstaltungsController

    @Autowired
    TeilnehmerService teilnehmerService;

    //returned immer true, aber kein doppelter Eintrag möglich
    @PostMapping(path= "/addStudent") // ohne Path
    public boolean fuegeTeilnehmerHinzu(@RequestBody TeilnahmeRequest request) {
        return teilnehmerService.fuegeTeilnehmerHinzu(request.userId(), request.courseId());
    }

    @PostMapping(path="/join") // kann raus, selbe wie fuegeTeilnehmerHinzu
    public boolean treteLVBei(@RequestBody Map<String, Integer> joinMap) {
        return teilnehmerService.fuegeTeilnehmerHinzu(joinMap.get("nutzerID"), joinMap.get("lehrveranstaltungsID"));
    }

    @GetMapping(path="/nutzer/{id}")
    public List<VeranstaltungsWrapper> zeigeLVEinesNutzers(@PathVariable("id") int nutzerId) {
        return teilnehmerService.erstelleTeilnahmeListeEinesNutzers(nutzerId);
    }

    @GetMapping(path="/lv/{id}")
    public List<NutzerWrapper> zeigeTeilnehmerEinerLV(@PathVariable("id") int lvID) {
        return teilnehmerService.erstelleTeilnehmerListeEinerLV(lvID);
    }

    @PostMapping(path="/check/{id}") // mit RequestParam arbeiten
    public boolean pruefeTeilnahme(@PathVariable("id") int nutzerId, @RequestBody int projektgruppenId) {
        return teilnehmerService.pruefeTeilnahme(nutzerId, projektgruppenId);
    }

    @PostMapping(path="/studentOf/{id}") // mit RequestParam arbeiten
    public boolean pruefeGemeinsameLehrveranstaltung(@PathVariable("id") int studentenId, @RequestBody int lehrendenId){
        return teilnehmerService.pruefeGemeinsameLehrveranstaltung(studentenId, lehrendenId);
    }

}
