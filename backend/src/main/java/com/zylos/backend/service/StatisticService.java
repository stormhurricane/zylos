package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.zylos.backend.controller.communication.NutzerWrapper;
import com.zylos.backend.controller.communication.StatistikWrapper;
import com.zylos.backend.database.*;
import com.zylos.backend.model.dto.EvaluationStatisticResponse;

import java.time.Year;
import java.util.*;

@Service
@Deprecated(since="2024-06", forRemoval=true)
public class StatisticService {

    @Autowired
    AttemptService attemptService;

    @Autowired
    FeedbackService feedbackService;

    @Autowired
    TestService testService;

    @Autowired
    TeilnehmerService teilnehmerService;

    @Autowired
    QuestionService questionService;

    @Autowired
    LehrveranstaltungsService lehrveranstaltungsService;

    @Autowired
    EmailService emailService;

    public double bestimmeTeilnahmequote(int testId) {

        List<Integer> teilgenommeneStudentenIds = this.bestimmeTeilgenommeneStudenten(testId);

        double teilgenommeneNutzer = teilgenommeneStudentenIds.stream().count();
        double gesamteNutzer = 0;
        List<NutzerWrapper> listeAllerTeilnehmerDerZugehoerigenLV = teilnehmerService.erstelleTeilnehmerListeEinerLV(testService.zeigeTestAn(testId).getLvId());
        for(NutzerWrapper nw: listeAllerTeilnehmerDerZugehoerigenLV){
            if(nw.getMoeglicherStudent() != null){
                gesamteNutzer++;
            }
        }
        return teilgenommeneNutzer/gesamteNutzer;
    }

    public double bestimmeBestehensquote(int testId) {
        List<Versuch> listeAllerVersuche = attemptService.findeAlleVersucheMitTestId(testId);
        List<Integer> teilgenommeneStudentenIds = this.bestimmeTeilgenommeneStudenten(testId);

        double gesamteNutzer = teilgenommeneStudentenIds.stream().count();
        double bestandeneTestAnzahl = 0;
        for(Integer nutzerId: teilgenommeneStudentenIds){
            for(Versuch versuch: listeAllerVersuche){
                if(nutzerId == versuch.getNutzerId() && versuch.isBestanden()){
                    bestandeneTestAnzahl++;
                    break;
                }
            }
        }
        return bestandeneTestAnzahl / gesamteNutzer;
    }

    public Map<Integer, Integer> bestimmteVersucheProTeilnehmer(int testId) {
        Map<Integer, Integer> versucheProTeilnehmer = new HashMap<>();
        List<Versuch> listeAllerVersuche = attemptService.findeAlleVersucheMitTestId(testId);
        List<Integer> teilgenommeneStudentenIds = this.bestimmeTeilgenommeneStudenten(testId);

        for(Integer studentID: teilgenommeneStudentenIds) {
            int versuche = 0;
            for(Versuch versuch: listeAllerVersuche) {
                if(studentID == versuch.getNutzerId()) {
                    versuche++;
                }
            }
            versucheProTeilnehmer.put(studentID, versuche);
        }
        return versucheProTeilnehmer;
    }

    public Map<Integer, Integer> bestimmeAnzahlKorrekterAntwortenEinerFrage(int testId) {
        Map<Integer, Integer> anzahlKorrekterAntwortenProFrage = new HashMap<>();
        List<Frage> frageList = questionService.findeAlleFragenMitTestId(testId);
        for(Frage frage: frageList) {
            List<Feedback> feedbackList = feedbackService.findeAlleFeedbacksMitFrageId(frage.getId());
            int anzahlKorrekterAntworten = 0;
            for(Feedback feedback: feedbackList) {
                if(feedback.isAbgegebeneAntwort()) {
                    anzahlKorrekterAntworten++;
                }
            }
            anzahlKorrekterAntwortenProFrage.put(frage.getId(), anzahlKorrekterAntworten);
        }
        return anzahlKorrekterAntwortenProFrage;
    }

    public StatistikWrapper erstelleStatistik(int testId) {
        return new StatistikWrapper(this.bestimmeTeilnahmequote(testId), this.bestimmeBestehensquote(testId), this.bestimmteVersucheProTeilnehmer(testId),
                this.bestimmeAnzahlKorrekterAntwortenEinerFrage(testId));
    }

    public List<Integer> bestimmeTeilgenommeneStudenten(int testId) {
        List<Versuch> listeAllerVersuche = attemptService.findeAlleVersucheMitTestId(testId);
        List<Integer> teilgenommeneStudentenIds = new ArrayList<>();

        for(Versuch versuch: listeAllerVersuche){
            if(!teilgenommeneStudentenIds.contains(versuch.getNutzerId())){
                teilgenommeneStudentenIds.add(versuch.getNutzerId());
            }
        }
        return teilgenommeneStudentenIds;
    }


    @Async
    public void pruefeBestehenNachSemesterende(Lehrveranstaltung.zeitEnum semesterZeit, String semesterJahr) {
        List<Lehrveranstaltung> zuPruefendeLV = lehrveranstaltungsService.findeLehrveranstaltungen(semesterZeit, semesterJahr);
        zuPruefendeLV.removeIf(r -> r instanceof Projektgruppe);

        for (Lehrveranstaltung lv : zuPruefendeLV) {

            List<Student_old> teilnehmerDerLV = teilnehmerService.erstelleStudentenListeEinerLV(lv.getLehrveranstaltungsID());

            if (teilnehmerDerLV.size() == 0) {
                continue;
            }

            for (Student_old teilnehmer : teilnehmerDerLV) {

                int nutzerId = teilnehmer.getId();
                boolean bestanden = this.pruefeBestehenEinesStudenten(nutzerId, lv.getLehrveranstaltungsID());

                emailService.generiereBestehensMail(teilnehmer.getVorname(), teilnehmer.getNachname(), teilnehmer.getEmail(),
                        lv.getTitel(), bestanden);

                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
    }


    // second minute hour day month weekday
    @Scheduled(cron = "0 0 0 1 4 ?")
    public void pruefeWinterSemester(){
        Lehrveranstaltung.zeitEnum semesterZeit = Lehrveranstaltung.zeitEnum.WS;
        int jahr = Year.now().getValue();
        String semesterJahr = (jahr-1) + "/" + jahr;

        this.pruefeBestehenNachSemesterende(semesterZeit, semesterJahr);
    }

    @Scheduled(cron = "0 0 0 1 10 ?")
    public void pruefeSommerSemester() {
        Lehrveranstaltung.zeitEnum semesterZeit = Lehrveranstaltung.zeitEnum.SS;
        String semesterJahr = String.valueOf(Year.now().getValue());

        this.pruefeBestehenNachSemesterende(semesterZeit, semesterJahr);
    }


    private boolean pruefeBestehenEinesStudenten(int studentenId, int lvId) {
        List<Test> testsDerLV = testService.zeigeTestlisteAn(lvId);
        int anzahlDerTests = testsDerLV.size();
        //Definition: gibt es keine Tests, haben automatisch alle bestanden.
        if (anzahlDerTests == 0) {
            return true;
        }

        int bestandeneTests = 0;

        for (Test test : testsDerLV) {
            List<Versuch> bestandeneVersuche = attemptService.findeBestandeneVersuche(studentenId, test.getId());
            if (bestandeneVersuche.size() > 0 ) {
                bestandeneTests++;
            }
        }

        if (bestandeneTests >= ((double) anzahlDerTests ) / 2) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public boolean checkStudentParticipation(int lvId, int studentenId) {
        List<Test> testsDerLV = testService.zeigeTestlisteAn(lvId);
        int anzahlDerTests = testsDerLV.size();

        if (anzahlDerTests == 0) {
            return true;
        }
        int teilgenommeneTests = 0;

        for (Test test: testsDerLV) {
            if (attemptService.pruefeVersuchsExistenz(studentenId, test.getId())) {
                teilgenommeneTests++;
            }
        }

        if (teilgenommeneTests >= ((double) (anzahlDerTests)) / 2) {
            return true;
        }
        else {
            return false;
        }
    }
    // Die Liste ist wie folgt aufgebaut: Die Liste enthält für jede Frage einen Array aus 5 Elementen: 0. Element FrageId 1-4. Element: Anzahl Antworten A-D
    public List<EvaluationStatisticResponse> getEvaluationStatistics(int testId, int filterStatus){
        List<BewertungsFeedback> bewertungsFeedbackListe = this.filtereAlleBewertungsfeedbacks(testId, filterStatus);
        List<Frage> alleFragenEinesTests = questionService.findeAlleFragenMitTestId(testId);
        List<EvaluationStatisticResponse> statistics = new ArrayList<>();

        for (Frage frage : alleFragenEinesTests) {
            int frageId = frage.getId();
            statistics.add(new EvaluationStatisticResponse(
                frageId,
                this.bestimmeAnzahlEinerAntwortEinerBewertungsfrage(frageId, 'A', bewertungsFeedbackListe),
                this.bestimmeAnzahlEinerAntwortEinerBewertungsfrage(frageId, 'B', bewertungsFeedbackListe),
                this.bestimmeAnzahlEinerAntwortEinerBewertungsfrage(frageId, 'C', bewertungsFeedbackListe),
                this.bestimmeAnzahlEinerAntwortEinerBewertungsfrage(frageId, 'D', bewertungsFeedbackListe)
            ));
        }
        return statistics;
    }

    public int bestimmeAnzahlEinerAntwortEinerBewertungsfrage(int frageId, char antwort, List<BewertungsFeedback> bewertungsFeedbackListe){
        int anzahl = 0;
        for(BewertungsFeedback bewertungsFeedback: bewertungsFeedbackListe){
            if(bewertungsFeedback.getFrageId() == frageId && bewertungsFeedback.getAntwort() == antwort){
                anzahl++;
            }
        }
        return anzahl;
    }

    public List<BewertungsFeedback> filtereAlleBewertungsfeedbacks(int testId, int bestanden) {
        List<BewertungsFeedback> bewertungsFeedbackListe = feedbackService.gibAlleBewertungsfeedbacksEinesTests(testId);
        if (bestanden == 0) {
            return bewertungsFeedbackListe;
        }
        int lvId = testService.zeigeTestAn(testId).getLvId();
        List<Integer> studentenIds = new ArrayList<>();
        for (BewertungsFeedback bewertungsFeedback : bewertungsFeedbackListe) {
            int studentenId = attemptService.gibVersuchMitVersuchId(bewertungsFeedback.getVersuchId()).getNutzerId();
            if (!studentenIds.contains(studentenId)) {
                studentenIds.add(studentenId);
            }
        }

        if (bestanden == -1) {
            for (int i = 0; i < studentenIds.size(); i++) {
                if (this.pruefeBestehenEinesStudenten(studentenIds.get(i), lvId)) {
                    studentenIds.remove(i);
                    i--;
                }
            }

        } else if (bestanden == 1) {
            for (int i = 0; i < studentenIds.size(); i++) {
                if (!this.pruefeBestehenEinesStudenten(studentenIds.get(i), lvId)) {
                    studentenIds.remove(i);
                    i--;
                }
            }
        }
        bewertungsFeedbackListe.clear();
        for (int studentenId : studentenIds) {
            bewertungsFeedbackListe.addAll(feedbackService.gibAlleBewertungsfeedbacksFuerVersuchsId(attemptService.gibVersuchMitNutzerIdUndTestId(studentenId, testId).getId()));
        }
        return bewertungsFeedbackListe;
    }
}
