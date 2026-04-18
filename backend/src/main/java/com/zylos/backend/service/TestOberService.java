package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.controller.communication.QuizWrapper;
import com.zylos.backend.controller.communication.StatistikWrapper;
import com.zylos.backend.database.Feedback;
import com.zylos.backend.database.Frage;
import com.zylos.backend.database.Lehrveranstaltung;
import com.zylos.backend.database.Test;
import com.zylos.backend.model.dto.CreateTestAttemptRequest;

import java.util.List;
import java.util.Map;

@Service
public class TestOberService {

    @Autowired
    TestService testService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FeedbackService feedbackService;

    @Autowired
    AttemptService attemptService;

    @Autowired
    StatisticService statisticService;

    public boolean legeQuizAn(QuizWrapper quizWrapper) {
        return testService.erstelleTest(quizWrapper);
    }

    public List<Test> zeigeAlleQuizEinerLV(int lvId) {
        return testService.zeigeTestlisteAn(lvId);
    }

    public List<Frage> zeigeAlleFragenEinesTests(int testId) {
        return questionService.findeAlleFragenMitTestId(testId);
    }

    public boolean legeFeedbackAn(Feedback feedback) {
        return feedbackService.erstelleFeedbackFürEineFrage(feedback);
    }

    public boolean legeFeedbackFürEinenVersuchAn(List<Feedback> feedback) {
        return feedbackService.erstelleFeedbackFürEinenVersuch(feedback);
    }

    public List<Feedback> zeigeFeedbackEinesVersuchs(int versuchId) {
        return feedbackService.findeAlleFeedbacksMitVersuchsId(versuchId);
    }

    public int createTestAttempt(CreateTestAttemptRequest testAttemptRequest) {
       return attemptService.createTestAttempt(testAttemptRequest);
    }

    public boolean pruefeVersuch(int versuchId) {
       return attemptService.bestimmeBestandenBeiVersuch(versuchId);
    }

    public StatistikWrapper zeigeStatistikEinesTests(int testId) {
        return statisticService.erstelleStatistik(testId);
    }

    public Frage findeFrageNameMitId(int frageId) {
        return questionService.findeFrageNameMitId(frageId);
    }

    public boolean erzwingeBestehenspruefung(Map<String, String> semesterDaten) {
        statisticService.pruefeBestehenNachSemesterende(Lehrveranstaltung.zeitEnum
                        .valueOf(semesterDaten.get("semesterZeit").toUpperCase()),
                semesterDaten.get("semesterJahr"));
        return true;
    }
}
