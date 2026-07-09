package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.FreundesListeID;
import com.zylos.backend.database.Freundschaft;
import com.zylos.backend.repository.FreundschaftRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Deprecated(since="2024-06", forRemoval=true)
public class FreundschaftService {

    @Autowired
    FreundschaftRepository freundschaftRepository;

    @Autowired
    NutzerService nutzerService;

    public List<Object> showFriendsOfUser(int nutzerId){
        //sequentiell, alternative wäre in der Schleife jedes Mal eine If-Abfrage.
        List<Freundschaft> alleFreundschaften = freundschaftRepository.findByFreundesListeID_NutzerId1(nutzerId);
        List <Object> alleFreunde = new ArrayList<>();
        for (Freundschaft freundschaft : alleFreundschaften) {
            if (freundschaft.isAkzeptiert()) {
                // Nutzer nutzer = nutzerService.findeNutzer(freundschaft.getFreundesListID().getNutzerId2());
                // if (nutzer != null) {
                //     alleFreunde.add(new NutzerResponse(nutzer));
                // }
            }
        }

        alleFreundschaften = freundschaftRepository.findByFreundesListeID_NutzerId2(nutzerId);

        for (Freundschaft freundschaft : alleFreundschaften) {
            if (freundschaft.isAkzeptiert()) {
                // Nutzer nutzer = nutzerService.findeNutzer(freundschaft.getFreundesListID().getNutzerId1());
                // if (nutzer != null) {
                //     alleFreunde.add(new NutzerResponse(nutzer));
                // }
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
    public List<Object> showOpenFriendRequests(int userId) {
        List <Object> nochNichtFreunde = new ArrayList<>();
        List<Freundschaft> alleFreundschaften = freundschaftRepository.findByFreundesListeID_NutzerId2(userId);

        for (Freundschaft freundschaft : alleFreundschaften) {
            if (!freundschaft.isAkzeptiert()) {
                // Nutzer nutzer = nutzerService.findeNutzer(freundschaft.getFreundesListID().getNutzerId1());
                // if (nutzer != null) {
                //     nochNichtFreunde.add(new NutzerResponse(nutzer));
                // }
            }
        }
        return nochNichtFreunde;
    }
}
