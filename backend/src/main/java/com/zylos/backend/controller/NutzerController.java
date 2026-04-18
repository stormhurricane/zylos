package com.zylos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.service.NutzerService;

import java.util.Map;

@RestController
@RequestMapping(path="api/v1/nutzer")
@Deprecated(since = "2026-04", forRemoval = true)
public class NutzerController {

    @Autowired
    NutzerService nutzerService;

    @Deprecated(since = "2026-04", forRemoval = true)
    //returns ID des Nutzers zurück, oder -1 falls nicht gefunden oder falsches PW
    @PostMapping(path="/login", consumes = "application/json", produces = "application/json")
    public int[] login(@RequestBody Map<String, String> loginDaten){
        return nutzerService.versucheLogin(loginDaten);
    }

    @Deprecated(since = "2026-04", forRemoval = true)
    @PostMapping(path="/verify/{id}", consumes = "application/json")
    public boolean verifiziereLogin(@PathVariable("id") int id,
                                    @RequestBody int code){
        return nutzerService.verfiziereLogin(id, code);
        //code ist der generierte verifizierungscode. id ist die id des nutzers
    }

}
