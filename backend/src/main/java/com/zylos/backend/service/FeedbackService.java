package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.BewertungsFeedback;
import com.zylos.backend.database.Feedback;
import com.zylos.backend.repository.FeedbackRepository;

import java.util.List;

@Service
@Deprecated(since="2024-06", forRemoval=true)
public class FeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;

    // @Autowired
    // QuestionService questionService;

    @Autowired
    EvaluationFeedbackService evaluationFeedbackService;

    public List<Feedback> findeAlleFeedbacksMitVersuchsId(int versuchId) {
       return feedbackRepository.findAllByVersuchId(versuchId);
    }

    public List<Feedback> findeAlleFeedbacksMitFrageId(int frageId) {
        return feedbackRepository.findAllByFrageId(frageId);
    }



    public boolean erstelleFeedbackFürEineFrage(Feedback feedback) {
        feedbackRepository.save(feedback);
        return true;
    }

    public boolean erstelleFeedbackFürEinenVersuch(List<Feedback> feedback) {
        for (Feedback feedback1 : feedback) {
            feedbackRepository.save(feedback1);
        }
        return true;
    }

    public List<BewertungsFeedback> gibAlleBewertungsfeedbacksEinesTests(int testId){
       return evaluationFeedbackService.gibAlleBewertungsfeedbacksEinesTests(testId);
    }

    public List<BewertungsFeedback> gibAlleBewertungsfeedbacksFuerEineFrage(int frageId){
        return evaluationFeedbackService.gibAlleBewertungsfeedbacksFuerEineFrage(frageId);
    }

    public List<BewertungsFeedback> gibAlleBewertungsfeedbacksFuerVersuchsId(int versuchId){
        return evaluationFeedbackService.gibAlleBewertungsfeedbacksFuerVersuchsId(versuchId);
    }
}
