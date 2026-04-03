package gruppei.backend.service;

import gruppei.backend.database.ArbeitsThema;
import gruppei.backend.repository.ArbeitsThemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArbeitsThemaService {

    @Autowired
    ArbeitsThemaRepository arbeitsThemaRepository;

    public boolean erstelleArbeitsThema(ArbeitsThema arbeitsThema){
        arbeitsThemaRepository.save(arbeitsThema);
        return true;
    }

    public List<ArbeitsThema> gibAlleArbeitsThemenEinesLehrenden(int lehrendenId){
        return arbeitsThemaRepository.findAllByLehrendenId(lehrendenId);
    }
}
