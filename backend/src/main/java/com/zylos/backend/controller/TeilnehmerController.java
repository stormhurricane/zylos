package com.zylos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.service.TeilnehmerService;


@RestController
@RequestMapping(path="api/v1/teilnehmerliste")
@Deprecated(since = "2026-04", forRemoval = true)
public class TeilnehmerController {

    // Aufteilung: Teils für Erzeugen und Löschen von Teilnehmern, Abfragen in den Nutzer oder VeranstaltungsController

    @Autowired
    TeilnehmerService teilnehmerService;


    @PostMapping(path="/studentOf/{id}") // mit RequestParam arbeiten
    public boolean pruefeGemeinsameLehrveranstaltung(@PathVariable("id") int studentenId, @RequestBody int lehrendenId){
        return teilnehmerService.pruefeGemeinsameLehrveranstaltung(studentenId, lehrendenId);
    }

}
