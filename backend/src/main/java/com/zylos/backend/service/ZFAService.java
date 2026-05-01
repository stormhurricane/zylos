package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.ZFA;
import com.zylos.backend.repository.ZFARepository;

@Service
@Deprecated(since="2024-06", forRemoval=true)
public class ZFAService {

    @Autowired
    private ZFARepository zfaRepository;

    public void fuegeZFAzu(int id, int code){
        ZFA zfa = new ZFA(id, code);
        ZFA vorherigeZFA = zfaRepository.findById(id);
        if(vorherigeZFA != null) {
            zfaRepository.delete(vorherigeZFA);
        }
        zfaRepository.save(zfa);
    }

    public int findCodeById(int id){
       ZFA zfa = zfaRepository.findById(id);
       return zfa.getCode();
    }

    public void loescheZFA(int id){
        zfaRepository.delete(zfaRepository.findById(id));
    }

}
