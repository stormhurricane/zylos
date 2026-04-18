package com.zylos.backend.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.Feedback;
import com.zylos.backend.model.dto.CreateTestAttemptRequest;
import com.zylos.backend.database.Frage;
import com.zylos.backend.database.Versuch;
import com.zylos.backend.repository.VersuchRepository;

import java.util.List;

@Service
public class AttemptService {

    private final VersuchRepository versuchRepository;
    private final QuestionService questionService;
    private final FeedbackService feedbackService;
    private final TestService testService;

    public AttemptService(
            VersuchRepository versuchRepository,
            QuestionService questionService,
            @Lazy FeedbackService feedbackService,
            @Lazy TestService testService) {
        this.versuchRepository = versuchRepository;
        this.questionService = questionService;
        this.feedbackService = feedbackService;
        this.testService = testService;
    }

    public int createTestAttempt(CreateTestAttemptRequest testAttemptRequest) {
        Versuch versuch = new Versuch(testAttemptRequest.userId(), testAttemptRequest.testId(), false);
        Versuch savedVersuch = versuchRepository.save(versuch);
        return savedVersuch.getId();
    }

    public boolean bestimmeBestandenBeiVersuch(int versuchId) {
        Versuch versuch = versuchRepository.findVersuchById(versuchId);
        List<Frage> alleFragenDesTests = questionService.findeAlleFragenMitTestId(versuch.getTestId());
        double fragenAnzahlDesTests = (double) alleFragenDesTests.stream().count();

        List<Feedback> feedbackAllerFragen = feedbackService.findeAlleFeedbacksMitVersuchsId(versuchId);
        double richtigBeantworteteFragen = 0;
        for(Feedback feedback: feedbackAllerFragen) {
            if(feedback.isAbgegebeneAntwort()) {
                richtigBeantworteteFragen++;
            }
        }

        if(richtigBeantworteteFragen / fragenAnzahlDesTests >= 0.5) {
           versuch.setBestanden(true);
        }
        versuchRepository.save(versuch);
        return versuch.isBestanden();
    }

    public List<Versuch> findeAlleVersucheMitTestId(int testId) {
        return versuchRepository.findAllByTestId(testId);
    }

    public List<Versuch> findeBestandeneVersuche(int nutzerId, int testId) {
        return versuchRepository.findAllByNutzerIdAndTestIdAndBestanden(nutzerId, testId, true);
    }

    public boolean pruefeVersuchsExistenz(int nutzerId, int testId) {
        return versuchRepository.findAllByNutzerIdAndTestId(nutzerId, testId).size() > 0;
    }

    public int createEvaluationAttempt(int userId, int courseId) {
        int testId = testService.getEvaluationIdByCourseId(courseId);
        Versuch versuch  = versuchRepository.findVersuchByNutzerIdAndTestId(userId, testId);
        if (versuch == null) {
            // per Definition true
            return versuchRepository.save(new Versuch(userId, testId, true)).getId();
        }
        else {
            return -1;
        }
    }

    public Versuch gibVersuchMitVersuchId(int versuchId){
        return versuchRepository.findVersuchById(versuchId);
    }

    public Versuch gibVersuchMitNutzerIdUndTestId(int nutzerId, int testId){
       return versuchRepository.findVersuchByNutzerIdAndTestId(nutzerId, testId);
    }
}
