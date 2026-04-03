package gruppei.backend.service;

import gruppei.backend.database.Frage;
import gruppei.backend.repository.FrageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrageService {

    @Autowired
    FrageRepository frageRepository;


    public boolean erstelleFrage(int testId,List<Frage> fragen) {
        for (Frage frage : fragen) {
            frage.setTestId(testId);
            frageRepository.save(frage);
        }
        return true;
    }

    public List<Frage> findeAlleFragenMitTestId(int testId) {
        return frageRepository.findAllByTestId(testId);
    }

    public Frage findeFrageNameMitId(int frageId) { return frageRepository.findById(frageId); }

    public List<Frage> zeigeBewertungsFragenEinerLv(int testId) {
        return frageRepository.findAllByTestId(testId);

    }

}
