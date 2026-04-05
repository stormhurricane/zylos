package com.zylos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.controller.communication.FreundschaftsAnfrage;
import com.zylos.backend.controller.communication.NutzerWrapper;
import com.zylos.backend.service.FreundschaftService;

import java.util.List;

@RestController
@RequestMapping(path="api/v2/friends")
public class FreundschaftController {

    @Autowired
    FreundschaftService freundschaftService;

    @GetMapping(path="/show/{id}")
    public List<NutzerWrapper> zeigeFreundeEinesNutzer(@PathVariable("id") int nutzerId) {
        return freundschaftService.findeFreundeEinesNutzers(nutzerId);
    }

    //true, falls bereits oder nun Freunde, sonst false
    @PostMapping(path="/sendRequest/{id}")
    public boolean sendeAnfrage(@PathVariable("id") int nutzerId1,
                                 @RequestBody int nutzerId2) {
       return freundschaftService.sendeFreundschaftsAnfrage(nutzerId1, nutzerId2);
    }

    @PostMapping(path="/respond")
    public boolean bearbeiteFreundschaftsAnfrage(@RequestBody FreundschaftsAnfrage anfrage) {
        return freundschaftService.schliesseFreundschaft(anfrage.getZuBearbeitendeAnfrage(),
                anfrage.isWirdAngenommen());
    }
    

    @GetMapping(path="/openRequests/{id}")
    public List<NutzerWrapper> zeigeneOffeneAnfragen(@PathVariable("id") int nutzerId) {
        return freundschaftService.zeigeneOffeneAnfragen(nutzerId);
    }
}
