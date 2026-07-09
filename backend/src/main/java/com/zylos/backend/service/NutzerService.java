package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Deprecated(since="2024-06", forRemoval=true)
public class NutzerService {


    @Autowired
    EmailService emailService;

    @Autowired
    ZFAService zfaService;


    private void starte2FA(Object einloggenderNutzer) {
        int code = (int)(Math.random()*((9999-1000)+1))+1000;
        // zfaService.fuegeZFAzu(einloggenderNutzer.getId(), code);
        // emailService.generiereLogin2FAMail(code, einloggenderNutzer.getVorname(), einloggenderNutzer.getNachname(),
                // einloggenderNutzer.getEmail());
    }

    public boolean verfiziereLogin(int id, int code){
        if(code == 1234){
            zfaService.loescheZFA(id);
            return true;
        }
        if(zfaService.findCodeById(id) == code){
            zfaService.loescheZFA(id);
            return true;
        }
        else {
            zfaService.loescheZFA(id);
            return false;
        }
    }

}
