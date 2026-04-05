package com.zylos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.controller.communication.NutzerWrapper;
import com.zylos.backend.controller.communication.VeranstaltungsWrapper;
import com.zylos.backend.service.TeilnehmerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="api/v1/teilnehmerliste")
public class TeilnehmerController {

    @Autowired
    TeilnehmerService teilnehmerService;

    //returned immer true, aber kein doppelter Eintrag möglich
    @PostMapping(path= "/addStudent")
    public boolean fuegeStudentHinzu(@RequestBody Map<String, Integer> addMap) {
        return teilnehmerService.fuegeTeilnehmerHinzu(addMap.get("studentenID"), addMap.get("lehrveranstaltungsID"));
    }

    @PostMapping(path="/join")
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

    @PostMapping(path="/check/{id}")
    public boolean pruefeTeilnahme(@PathVariable("id") int nutzerId, @RequestBody int projektgruppenId) {
        return teilnehmerService.pruefeTeilnahme(nutzerId, projektgruppenId);
    }

    @PostMapping(path="/studentOf/{id}")
    public boolean pruefeGemeinsameLehrveranstaltung(@PathVariable("id") int studentenId, @RequestBody int lehrendenId){
        return teilnehmerService.pruefeGemeinsameLehrveranstaltung(studentenId, lehrendenId);
    }

}
