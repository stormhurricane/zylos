package gruppei.backend.service;

import gruppei.backend.database.BewertungsFeedback;
import gruppei.backend.database.Feedback;
import gruppei.backend.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    FrageService frageService;

    @Autowired
    BewertungsFeedbackService bewertungsFeedbackService;

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
       return bewertungsFeedbackService.gibAlleBewertungsfeedbacksEinesTests(testId);
    }

    public List<BewertungsFeedback> gibAlleBewertungsfeedbacksFuerEineFrage(int frageId){
        return bewertungsFeedbackService.gibAlleBewertungsfeedbacksFuerEineFrage(frageId);
    }

    public List<BewertungsFeedback> gibAlleBewertungsfeedbacksFuerVersuchsId(int versuchId){
        return bewertungsFeedbackService.gibAlleBewertungsfeedbacksFuerVersuchsId(versuchId);
    }
}
