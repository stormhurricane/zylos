package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.zylos.backend.controller.communication.NutzerWrapper;
import com.zylos.backend.database.Nutzer;
import com.zylos.backend.database.Reminder;
import com.zylos.backend.database.Termin;
import com.zylos.backend.repository.ReminderRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReminderService {

    @Autowired
    ReminderRepository reminderRepository;

    @Autowired
    TeilnehmerService teilnehmerService;

    @Autowired
    TerminService terminService;

    @Autowired
    NutzerService nutzerService;

    @Autowired
    EmailService emailService;

    public boolean legeReminderFuerAlleNutzerDerLvAn(Reminder reminder){
        List<NutzerWrapper> listeAllerTeilnehmerEinerLV = teilnehmerService.erstelleTeilnehmerListeEinerLV(terminService.findeTerminMitId(reminder.getTerminId()).getlvId());
        for(NutzerWrapper nw : listeAllerTeilnehmerEinerLV){
            Reminder zuSpeichernderReminder = new Reminder(reminder.getTerminId(), reminder.getJahr(), reminder.getMonat(), reminder.getTag(), reminder.getUhrzeit(), reminder.getForm());
            if(nw.getMoeglicherStudent() != null){
                zuSpeichernderReminder.setNutzerId(nw.getMoeglicherStudent().getId());
            } else {
                zuSpeichernderReminder.setNutzerId(nw.getMoeglicherLehrender().getId());
            }
            reminderRepository.save(zuSpeichernderReminder);
        }
        return true;
    }

    public List<Reminder> gibAlleFaelligenReminderAus(int nutzerId, Map<String, String> dateTimeMap) {
        List<Reminder> alleReminderDesNutzers = reminderRepository.findAllByNutzerIdAndForm(nutzerId, Reminder.FormEnum.POPUP);
        List<Reminder> faelligeReminder = new ArrayList<>();
        for(Reminder reminder : alleReminderDesNutzers){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String dateString = dateTimeMap.get("date");
            LocalDate aktuellesDatum = LocalDate.parse(dateString, dateTimeFormatter);
            LocalDate reminderDatum = LocalDate.of(Integer.parseInt(reminder.getJahr()), Integer.parseInt(reminder.getMonat()), Integer.parseInt(reminder.getTag()));
            if (reminderDatum.compareTo(aktuellesDatum) <= -1) {
                faelligeReminder.add(reminder);
                reminderRepository.delete(reminder);
            } else if (reminderDatum.compareTo(aktuellesDatum) == 0
                    && LocalTime.parse(reminder.getUhrzeit()).compareTo(LocalTime.parse(dateTimeMap.get("time"))) <= 0) {
                faelligeReminder.add(reminder);
                reminderRepository.delete(reminder);
            }
        }
        return faelligeReminder;
    }


    @Scheduled(fixedDelay = 60000)
    @Async
    public void sucheFaelligeEmailReminder(){
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        List<Reminder> alleEmailReminder = reminderRepository.findAllByForm(Reminder.FormEnum.EMAIL);
        if (alleEmailReminder.size() > 0) {
            alleEmailReminder.removeIf(r -> currentDate.compareTo(LocalDate.of(Integer.parseInt(r.getJahr()), Integer.parseInt(r.getMonat()), Integer.parseInt(r.getTag()))) < 0 );

            alleEmailReminder.removeIf(r -> LocalTime.parse(r.getUhrzeit()).compareTo(currentTime) > 0
                    && currentDate.compareTo(LocalDate.of(Integer.parseInt(r.getJahr()), Integer.parseInt(r.getMonat()), Integer.parseInt(r.getTag()))) <= 0
            );

            for (Reminder reminder : alleEmailReminder) {
                Termin referenzierterTermin = terminService.findeTerminMitId(reminder.getTerminId());
                Nutzer nutzer = nutzerService.findeNutzer(reminder.getNutzerId());
                emailService.generiereReminderEmail(nutzer.getVorname(), nutzer.getNachname(), nutzer.getEmail(), referenzierterTermin.getBetreff(),
                        LocalDate.of(Integer.parseInt(referenzierterTermin.getJahr()), Integer.parseInt(referenzierterTermin.getMonat()),
                                Integer.parseInt(referenzierterTermin.getTag())), referenzierterTermin.getUhrzeit());
                reminderRepository.delete(reminder);
                try {
                    //Thread nach jeder Email schlafen schicken, da sonst ein Error vom Mail-Sender kommt.
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
            }
        }
    }

    public boolean fuegeNachzueglerHinzu(int nutzerId, int lvId) {
        List<Termin> termineEinerLV = terminService.findeTermineEinerLv(lvId);

        for (Termin t: termineEinerLV) {
            Reminder neuerReminder;
            List<Reminder> bisherigeReminder = reminderRepository.findAllByTerminId(t.getId());
            if (bisherigeReminder.size() > 0 ) {
                Reminder bisherigerReminder = bisherigeReminder.get(0);
                neuerReminder = new Reminder(bisherigerReminder.getTerminId(), bisherigerReminder.getJahr(),bisherigerReminder.getMonat(), bisherigerReminder.getTag(),
                        bisherigerReminder.getUhrzeit(), bisherigerReminder.getForm());
                neuerReminder.setNutzerId(nutzerId);
                reminderRepository.save(neuerReminder);
            }
        }
        return true;
    }
}
