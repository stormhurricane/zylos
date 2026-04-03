package gruppei.backend.service;

import gruppei.backend.database.LernkartenThema;
import gruppei.backend.repository.LernkartenThemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LernkartenThemaService {

    @Autowired
    LernkartenThemaRepository lernkartenThemaRepository;

    //Beschreibung innerhalb einer lvId muss unique sein
    public int LernkartenThemaErstellen(int lvId, String beschreibung) {
        LernkartenThema lernkartenThema = lernkartenThemaRepository.findByLvIdAndBeschreibung(lvId, beschreibung);
        if (lernkartenThema == null) {
            return (lernkartenThemaRepository.save(new LernkartenThema(beschreibung, lvId))).getId();
        }
        return -1;
    }

    public List<LernkartenThema> ListeAllerLernkartenThemen(int lvId) {
        return lernkartenThemaRepository.findAllByLvId(lvId);
    }
}
