package gruppei.backend.service;

import gruppei.backend.database.LehrveranstaltungsMaterial;
import gruppei.backend.repository.LehrveranstaltungsMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LehrveranstaltungsMaterialService {

    @Autowired
    LehrveranstaltungsService lehrveranstaltungsService;

    @Autowired
    LehrveranstaltungsMaterialRepository lehrveranstaltungsMaterialRepository;


    public List<LehrveranstaltungsMaterial> erstelleMaterialListe(int lehrveranstaltungsId) {

        List<LehrveranstaltungsMaterial> lehrveranstaltungsMaterials = lehrveranstaltungsMaterialRepository.findAll();
        List<LehrveranstaltungsMaterial> returnList = new ArrayList<>();
        for (LehrveranstaltungsMaterial lehrveranstaltungsMaterial:lehrveranstaltungsMaterials) {
            if (lehrveranstaltungsMaterial.getLehrveranstaltungsId()==lehrveranstaltungsId) {
                returnList.add(lehrveranstaltungsMaterial);
            }
        }
        return returnList;
    }

    public boolean fuegeMaterialHinzu(LehrveranstaltungsMaterial lehrveranstaltungsMaterial) {
          lehrveranstaltungsMaterialRepository.save(lehrveranstaltungsMaterial);
          return true;
    }


}
