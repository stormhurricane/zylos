package gruppei.backend.controller;

import gruppei.backend.controller.communication.QuizWrapper;
import gruppei.backend.controller.communication.StatistikWrapper;
import gruppei.backend.database.Feedback;
import gruppei.backend.database.Frage;
import gruppei.backend.database.Test;
import gruppei.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="api/v2/quiz")
public class QuizController {

   @Autowired
   TestOberService testOberService;

    @PostMapping(path="/new")
    public boolean legeQuizAn(@RequestBody QuizWrapper quizWrapper) {
        return testOberService.legeQuizAn(quizWrapper);
    }

    @GetMapping(path="/showFrom/{id}")
    public List<Test> zeigeAlleQuizEinerLV(@PathVariable("id") int lvId) {
        return testOberService.zeigeAlleQuizEinerLV(lvId);
    }

    @GetMapping(path="/showQuestions/{id}")
    public List<Frage> zeigeAlleFragenEinesTests(@PathVariable("id") int testId) {
        return testOberService.zeigeAlleFragenEinesTests(testId);
    }

    @GetMapping(path="findeFrageMitId/{id}", produces = "application/json")
    public Frage findeFrageMitId(@PathVariable("id") int frageId){
        return testOberService.findeFrageNameMitId(frageId);
    }

    @PostMapping(path="/feedback")
    public boolean legeFeedbackAn(@RequestBody Feedback feedback) {
       return testOberService.legeFeedbackAn(feedback);
    }

    @PostMapping(path="/feedbackVersuch")
    public boolean legeFeedbackFürEinenVersuchAn(@RequestBody List<Feedback> feedback) {
        return testOberService.legeFeedbackFürEinenVersuchAn(feedback);
    }

    @GetMapping(path="/showFeedback/{id}")
    public List<Feedback> zeigeFeedbackEinesVersuchs(@PathVariable("id") int versuchId) {
        return testOberService.zeigeFeedbackEinesVersuchs(versuchId);
    }

    @PostMapping(path="/versuch")
    public int legeVersuchAn(@RequestBody Map<String, Integer> versuch) {
        return testOberService.legeVersuchAn(versuch);
    }

    @PutMapping(path="/pruefeVersuch/")
    public boolean pruefeVersuch(@RequestBody int versuchId) {
        return testOberService.pruefeVersuch(versuchId);
    }

    @GetMapping(path="/statistik/{id}")
    public StatistikWrapper zeigeStatistikEinesTests(@PathVariable("id") int testId) {
        return testOberService.zeigeStatistikEinesTests(testId);
    }

    @PostMapping(path="/forceSemester")
    public boolean erzwingeBestehenspruefung(@RequestBody Map<String, String> semesterDaten) {
        testOberService.erzwingeBestehenspruefung(semesterDaten);
        return true;
    }
}
