package gruppei.backend.service;

import gruppei.backend.database.BewertungsFeedback;
import gruppei.backend.database.Frage;
import gruppei.backend.database.Versuch;
import gruppei.backend.repository.BewertungsFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BewertungsFeedbackService {

    @Autowired
    BewertungsFeedbackRepository bewertungsFeedbackRepository;

    @Autowired
    VersuchService versuchService;

    public boolean erstelleFeedbackFürEinenVersuch(List<BewertungsFeedback> BewertungsFeedbacks) {
        for (BewertungsFeedback bewertungsFeedback : BewertungsFeedbacks) {
            bewertungsFeedbackRepository.save(bewertungsFeedback);
        }
        return true;
    }

    public List<BewertungsFeedback> gibAlleBewertungsfeedbacksEinesTests(int testId){
        List<Versuch> versuchListe = versuchService.findeAlleVersucheMitTestId(testId);
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
