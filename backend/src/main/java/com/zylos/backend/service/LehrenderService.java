package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.Lehrender;
import com.zylos.backend.repository.LehrenderRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LehrenderService {

    @Autowired
    LehrenderRepository lehrenderRepository;


    public boolean aendereLehrender(Lehrender lehrender, Map<String, String> changeData) {
        if (changeData.isEmpty()) return true;
        if (changeData.containsKey("adresse")) {
            lehrender.setAdresse(changeData.get("adresse"));
        }
        if (changeData.containsKey("profilbild")) {
            lehrender.setProfilbild(changeData.get("profilbild"));
        }
        if (changeData.containsKey("passwort")) {
            lehrender.setPasswort(changeData.get("passwort"));
        }
        if (changeData.containsKey("lehrstuhl")) {
            lehrender.setLehrstuhl(changeData.get("lehrstuhl"));
        }
        if (changeData.containsKey("forschungsgebiet")) {
            lehrender.setForschungsgebiet(changeData.get("forschungsgebiet"));
        }

        lehrenderRepository.save(lehrender);
        return true;
    }

    public Lehrender findeLehrender(int id) {
        Optional<Lehrender> lehrender = lehrenderRepository.findById(id);
        if (lehrender.isPresent()) {
            Lehrender existierenderLehrender = lehrender.get();
            return existierenderLehrender;
        }
        else { return null;}
    }

    public List<Lehrender> gibAlleLehrende() {
        return lehrenderRepository.findAll();
    }


    public Lehrender login(String email, String passwort) {
        Lehrender einloggenderLehrer = lehrenderRepository.findLehrenderByEmail(email);
        if (einloggenderLehrer != null) {
            if (einloggenderLehrer.getPasswort().equals(passwort)) {
                return einloggenderLehrer;
            }
        }
        return null;
    }

    public boolean registriereLehrender(Lehrender lehrender) {
        lehrenderRepository.save(lehrender);
        return true;
    }

    public Lehrender ueberpruefeEmail(String email) {
        return lehrenderRepository.findLehrenderByEmail(email);
    }

}
