package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.Lehrender;
import com.zylos.backend.repository.LehrenderRepository;

import java.util.List;
import java.util.Optional;

@Service
@Deprecated(since="2024-06", forRemoval=true)
public class LehrenderService {

    @Autowired
    LehrenderRepository lehrenderRepository;

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

}
