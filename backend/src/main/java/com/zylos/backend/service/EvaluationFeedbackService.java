package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.model.dto.CreateEvaluationFeedbackRequest;
import com.zylos.backend.database.BewertungsFeedback;
import com.zylos.backend.database.Frage;
import com.zylos.backend.database.Versuch;
import com.zylos.backend.repository.BewertungsFeedbackRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class EvaluationFeedbackService {

    @Autowired
    BewertungsFeedbackRepository bewertungsFeedbackRepository;

    @Autowired
    AttemptService attemptService;

    public boolean createEvaluationFeedbackForAttempt(List<CreateEvaluationFeedbackRequest> feedbackRequests) {
        for (CreateEvaluationFeedbackRequest request : feedbackRequests) {
            BewertungsFeedback feedback = new BewertungsFeedback(
                request.attemptId(),
                request.questionId(),
                request.givenAnswerIsCorrect(),
                request.selectedAnswer()
            );
            bewertungsFeedbackRepository.save(feedback);
        }
        return true;
    }

    public List<BewertungsFeedback> gibAlleBewertungsfeedbacksEinesTests(int testId){
        List<Versuch> versuchListe = attemptService.findeAlleVersucheMitTestId(testId);
        List<BewertungsFeedback> bewertungsFeedbackListe = new ArrayList<>();
        for(Versuch versuch: versuchListe){
            bewertungsFeedbackListe.addAll(bewertungsFeedbackRepository.findAllByVersuchId(versuch.getId()));
        }
        return bewertungsFeedbackListe;
    }

    public List<BewertungsFeedback> gibAlleBewertungsfeedbacksFuerEineFrage(int frageId){
        return bewertungsFeedbackRepository.findAllByFrageId(frageId);
    }

    public List<BewertungsFeedback> gibAlleBewertungsfeedbacksFuerVersuchsId(int versuchId){
       return bewertungsFeedbackRepository.findAllByVersuchId(versuchId);
    }
}
