package com.zylos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.database.Reminder;
import com.zylos.backend.model.dto.CreateAppointmentRequest;
import com.zylos.backend.model.dto.CreateReminderRequest;
import com.zylos.backend.service.ReminderService;
import com.zylos.backend.model.dto.TerminResponse;
import com.zylos.backend.service.TerminService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="api/v2/calender")
public class KalenderController {

    // in 2 Controller aufteilen. Reminder und Appointments

    @Autowired
    TerminService terminService;

    @Autowired
    ReminderService reminderService;

    //(terminId rückgabe) / bei Fehler wert <0
    @PostMapping(path="/erstelleTermin") // Post ohne path
    public int legeTerminAn(@RequestBody CreateAppointmentRequest request) {
        return terminService.legeTerminAn(request);
    }

    @PostMapping(path="/termineEinesDatums/{id}") // wird Get!
    public List<TerminResponse> termineEinesDatums (@PathVariable ("id") int nutzerId,
                                            @RequestBody Map<String, Integer> dateMap) {
        return terminService.terminListeEinesNutzers(nutzerId, dateMap);
    }

    @PostMapping(path="/reminderEinesTermins")
    public boolean reminderFuerTerminErstellen(@RequestBody CreateReminderRequest request){
        return reminderService.legeReminderFuerAlleNutzerDerLvAn(request);
    }

    // dateTimeMap: 1. Key = "date" 2. Key = "time"
    @PostMapping(path= "/reminderFuerPopUpSchicken/{id}") // wird get
    public List<Reminder> schickeAlleFaelligenReminderEinesNutzers(@PathVariable("id") int nutzerId,
                                                                   @RequestBody Map<String, String> dateTimeMap){
        return reminderService.gibAlleFaelligenReminderAus(nutzerId, dateTimeMap);
    }

    @GetMapping(path="/terminVonReminder/{id}")
    public TerminResponse gebeTerminEinesReminders(@PathVariable("id") int terminID) {
        return terminService.findeTerminMitId(terminID);
    }

}
