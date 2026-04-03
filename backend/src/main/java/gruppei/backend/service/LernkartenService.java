package gruppei.backend.service;

import gruppei.backend.database.Lernkarte;
import gruppei.backend.repository.LernkartenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LernkartenService {

    @Autowired
    LernkartenRepository lernkartenRepository;

    public boolean LernkarteErstellen(Lernkarte lernkarte) {
        lernkartenRepository.save(lernkarte);
        return true;
    }

    public List<Lernkarte> ListeLernkartenEinesThemas(int lernkartenThemaId) {
        return lernkartenRepository.findAllByLernkartenThemaId(lernkartenThemaId);
    }

}
