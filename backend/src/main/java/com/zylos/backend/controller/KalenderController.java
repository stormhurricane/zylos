package com.zylos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.database.Reminder;
import com.zylos.backend.database.Termin;
import com.zylos.backend.service.ReminderService;
import com.zylos.backend.service.TerminService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="api/v2/calender")
public class KalenderController {

    @Autowired
    TerminService terminService;

    @Autowired
    ReminderService reminderService;

    //(terminId rückgabe) / bei Fehler wert <0
    @PostMapping(path="/erstelleTermin")
    public int legeTerminAn(@RequestBody Termin termin) {
        return terminService.legeTerminAn(termin);
    }

    @PostMapping(path="/termineEinesDatums/{id}")
    public List<Termin> termineEinesDatums (@PathVariable ("id") int nutzerId,
                                            @RequestBody Map<String, Integer> dateMap) {
        return terminService.terminListeEinesNutzers(nutzerId, dateMap);
    }

    @PostMapping(path="/reminderEinesTermins")
    public boolean reminderFuerTerminErstellen(@RequestBody Reminder reminder){
        return reminderService.legeReminderFuerAlleNutzerDerLvAn(reminder);
    }

    // dateTimeMap: 1. Key = "date" 2. Key = "time"
    @PostMapping(path= "/reminderFuerPopUpSchicken/{id}")
    public List<Reminder> schickeAlleFaelligenReminderEinesNutzers(@PathVariable("id") int nutzerId,
                                                                   @RequestBody Map<String, String> dateTimeMap){
        return reminderService.gibAlleFaelligenReminderAus(nutzerId, dateTimeMap);
    }

    @GetMapping(path="/terminVonReminder/{id}")
    public Termin gebeTerminEinesReminders(@PathVariable("id") int terminID) {
        return terminService.findeTerminMitId(terminID);
    }

}
