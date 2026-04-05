package com.zylos.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.BewertungsFeedback;
import com.zylos.backend.database.Lehrender;

import java.util.List;

@Repository
public interface BewertungsFeedbackRepository extends JpaRepository<BewertungsFeedback, Integer> {

    List<BewertungsFeedback> findAllByVersuchId(int versuchId);

    List<BewertungsFeedback> findAllByFrageId(int frageId);
}
