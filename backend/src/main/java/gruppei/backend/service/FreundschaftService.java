package gruppei.backend.service;

import gruppei.backend.controller.communication.NutzerWrapper;
import gruppei.backend.database.FreundesListeID;
import gruppei.backend.database.Freundschaft;
import gruppei.backend.repository.FreundschaftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FreundschaftService {

    @Autowired
    FreundschaftRepository freundschaftRepository;

    @Autowired
    NutzerService nutzerService;

    public List<NutzerWrapper> findeFreundeEinesNutzers(int nutzerId){
        //sequentiell, alternative wäre in der Schleife jedes Mal eine If-Abfrage.
        List<Freundschaft> alleFreundschaften = freundschaftRepository.findByFreundesListeID_NutzerId1(nutzerId);
        List <NutzerWrapper> alleFreunde = new ArrayList<>();
        for (Freundschaft freundschaft : alleFreundschaften) {
            if (freundschaft.isAkzeptiert()) {
                alleFreunde.add(new NutzerWrapper(nutzerService.findeNutzer(freundschaft.getFreundesListID().getNutzerId2())));
            }
        }

        alleFreundschaften = freundschaftRepository.findByFreundesListeID_NutzerId2(nutzerId);

        for (Freundschaft freundschaft : alleFreundschaften) {
            if (freundschaft.isAkzeptiert()) {
                alleFreunde.add(new NutzerWrapper(nutzerService.findeNutzer(freundschaft.getFreundesListID().getNutzerId1())));
            }
        }

        return alleFreunde;
    }

    //sendet true, wenn bereits Freunde oder jetzt Freunde
    //false bei Anfrage gesendet
    public boolean sendeFreundschaftsAnfrage(int nutzerId1, int nutzerId2) {
        Freundschaft existierendeFreundschaft = this.findeExistierendeFreundschaft(nutzerId1, nutzerId2);

        if (existierendeFreundschaft != null) {
            if (!existierendeFreundschaft.isAkzeptiert()) {
                existierendeFreundschaft.setAkzeptiert(true);
                freundschaftRepository.save(existierendeFreundschaft);
            }
            return true;
        }
        else {
            freundschaftRepository.save(new Freundschaft(new FreundesListeID(nutzerId1, nutzerId2)));
            return false;
        }
    }

    //sucht eine mögliche existierendeFreundschaft zweier Nutzer. Kann null sein.
    private Freundschaft findeExistierendeFreundschaft (int nutzerId1, int nutzerId2) {
        Freundschaft moeglicheFreundschaftV1 = freundschaftRepository.findByFreundesListeID(new FreundesListeID(nutzerId1, nutzerId2));
        Freundschaft moeglicheFreundschaftV2 = freundschaftRepository.findByFreundesListeID(new FreundesListeID(nutzerId2, nutzerId1));

        if (moeglicheFreundschaftV1 != null) {return moeglicheFreundschaftV1;}
        else if (moeglicheFreundschaftV2 != null) {return moeglicheFreundschaftV2;}
        else return null;
    }

    //true, wenn Freundschaft angenommen, false falls nicht
    public boolean schliesseFreundschaft(int[] ids, boolean antwort){
        Freundschaft freundschaft = findeExistierendeFreundschaft(ids[0], ids[1]);

        if (antwort) {
            freundschaft.setAkzeptiert(true);
            freundschaftRepository.save(freundschaft);
        }
        else {
            freundschaftRepository.delete(freundschaft);
        }
        return antwort;
    }

    //Ausnutzen der Datenstruktur, dass neue Freundschaften immer eigene nutzerID als nutzerID 1 anlegen
    //Das ist so dirty.
    public List<NutzerWrapper> zeigeneOffeneAnfragen(int nutzerId) {
//        List<Freundschaft> alleFreundschaften = freundschaftRepository.findByFreundesListeID_NutzerId1(nutzerId);
        List <NutzerWrapper> nochNichtFreunde = new ArrayList<>();
//        for (Freundschaft freundschaft : alleFreundschaften) {
//            if (!freundschaft.isAkzeptiert()) {
//                nochNichtFreunde.add(new NutzerWrapper(nutzerService.findeNutzer(freundschaft.getFreundesListID().getNutzerId2())));
//            }
//        }

        List<Freundschaft> alleFreundschaften = freundschaftRepository.findByFreundesListeID_NutzerId2(nutzerId);

        for (Freundschaft freundschaft : alleFreundschaften) {
            if (!freundschaft.isAkzeptiert()) {
                nochNichtFreunde.add(new NutzerWrapper(nutzerService.findeNutzer(freundschaft.getFreundesListID().getNutzerId1())));
            }
        }

        return nochNichtFreunde;
    }
}
