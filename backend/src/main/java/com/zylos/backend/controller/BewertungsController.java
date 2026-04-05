package com.zylos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.controller.communication.QuizWrapper;
import com.zylos.backend.database.BewertungsFeedback;
import com.zylos.backend.database.Frage;
import com.zylos.backend.service.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v3/lvBewertung")
public class BewertungsController {

    @Autowired
    TestService testService;

    @Autowired
    VersuchService versuchService;

    @Autowired
    BewertungsFeedbackService bewertungsFeedbackService;

    @Autowired
    StatistikService statistikService;

    @PostMapping(path="/create")
    public boolean erstelleBewertung(@RequestBody QuizWrapper quizWrapper) {
        return testService.erstelleBewertung(quizWrapper);
    }

    @GetMapping(path="/showQuestions/{id}")
    public List<Frage> zeigeBewertungsFragenEinerLv(@PathVariable ("id") int lvId) {
        return testService.zeigeBewertungsFragenEinerLv(lvId);
    }

    @PostMapping(path="/check/{id}")
    public int erstelleBerwertungsVersuch(@PathVariable ("id") int nutzerId,
                                          @RequestBody int lvId) {
        return versuchService.erstelleBerwertungsVersuch(nutzerId, lvId);
    }

    @PostMapping(path="/createFeedback")
    public boolean erstelleFeedbackFürEinenVersuch(@RequestBody List<BewertungsFeedback> bewertungsFeedbackList) {
        return bewertungsFeedbackService.erstelleFeedbackFürEinenVersuch(bewertungsFeedbackList);
    }

    // Erläuterung int bestanden: -1 nicht bestanden, 0 kein Filter, 1 bestanden
    @PostMapping(path="/createFeedbackStatistics/{id}")
    public List<int[]> erstelleBewertungsStatistik(@PathVariable("id") int testId,
                                                   @RequestBody int bestanden){
        return statistikService.erstelleBewertungsstatistik(testId, bestanden);
    }

    @PostMapping(path="/checkParticipation/{id}")
    public boolean pruefeTeilnahmeEinesStudenten(@PathVariable("id") int studentenId,
                                                 @RequestBody int lvId) {
        return statistikService.pruefeTeilnahmeEinesStudenten(lvId, studentenId);
    }

}
