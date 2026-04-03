package gruppei.backend.service;

import gruppei.backend.database.Feedback;
import gruppei.backend.database.Frage;
import gruppei.backend.database.Versuch;
import gruppei.backend.repository.VersuchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VersuchService {

    @Autowired
    VersuchRepository versuchRepository;

    @Autowired
    FrageService frageService;

    @Autowired
    FeedbackService feedbackService;

    @Autowired
    TestService testService;

    //Key 1 "nutzerId", Key 2 "testId"
    public int erstelleVersuch(Map<String, Integer> versuchMap) {
        versuchRepository.save(new Versuch(versuchMap.get("nutzerId"), versuchMap.get("testId"),false));
        List<Versuch> moeglicheIds = versuchRepository.findAllByNutzerIdAndTestId(versuchMap.get("nutzerId"), versuchMap.get("testId"));
        int id = 0;
        for(Versuch versuch: moeglicheIds){
            if(versuch.getId() > id){
                id = versuch.getId();
            }
        }
        return id;
    }

    public boolean bestimmeBestandenBeiVersuch(int versuchId) {
        Versuch versuch = versuchRepository.findVersuchById(versuchId);
        List<Frage> alleFragenDesTests = frageService.findeAlleFragenMitTestId(versuch.getTestId());
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

    public int erstelleBerwertungsVersuch(int nutzerid, int lvId) {
        int testId = testService.zeigeBewertungsIdAn(lvId);
        Versuch versuch  = versuchRepository.findVersuchByNutzerIdAndTestId(nutzerid, testId);
        if (versuch == null) {
            // per Definition true
            return versuchRepository.save(new Versuch(nutzerid, testId, true)).getId();
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
