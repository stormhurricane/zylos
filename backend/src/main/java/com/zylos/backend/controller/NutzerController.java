package com.zylos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.controller.communication.NutzerWrapper;
import com.zylos.backend.database.Lehrender;
import com.zylos.backend.database.Nutzer;
import com.zylos.backend.database.Student;
import com.zylos.backend.service.NutzerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="api/v1/nutzer")
public class NutzerController {

    @Autowired
    NutzerService nutzerService;

    @PutMapping(path="/update/{id}")
    public boolean aendereProfilDaten(@PathVariable("id") int id,
                                      @RequestBody Map<String, String> changeData) {
        return nutzerService.aendereProfil(id, changeData);
    }

    @PostMapping(path="/findStudent", consumes = "application/json", produces = "application/json")
    public List<Integer> findeStudentIds(@RequestBody Map<String, String> suchDaten){
        return nutzerService.findeStudenten(suchDaten);
    }

    //returns ID des Nutzers zurück, oder -1 falls nicht gefunden oder falsches PW
    @PostMapping(path="/login", consumes = "application/json", produces = "application/json")
    public int[] login(@RequestBody Map<String, String> loginDaten){
        return nutzerService.versucheLogin(loginDaten);
    }

    @PostMapping(path="/register", params = "nutzer=lehrender")
    public boolean registriereLehrender(@RequestBody Lehrender lehrender) {
        return nutzerService.registriereLehrender(lehrender);
    }

    @PostMapping(path="/register", params = "nutzer=student")
    public boolean registriereStudent(@RequestBody Student student){
        return nutzerService.registriereStudent(student);
    }

    @GetMapping(path="/view/{id}", produces = "application/json")
    public NutzerWrapper rufeNutzerProfilAuf(@PathVariable("id") int id) {
        return new NutzerWrapper(nutzerService.findeNutzer(id));
    }

    @PostMapping(path="/verify/{id}", consumes = "application/json")
    public boolean verifiziereLogin(@PathVariable("id") int id,
                                    @RequestBody int code){
        return nutzerService.verfiziereLogin(id, code);
        //code ist der generierte verifizierungscode. id ist die id des nutzers
    }

    //gibt Liste aller Nutzer aus - TestMethode
    @GetMapping(path="/all", produces = "application/json")
    public List<Nutzer> zeigeAlleNutzer(){
        return nutzerService.erzeugeNutzerListe();
    }

}
