package com.zylos.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.BewertungsFeedback;

import java.util.List;

@Repository
@Deprecated(since = "2024-06", forRemoval = true)
public interface BewertungsFeedbackRepository extends JpaRepository<BewertungsFeedback, Integer> {

    List<BewertungsFeedback> findAllByVersuchId(int versuchId);

    List<BewertungsFeedback> findAllByFrageId(int frageId);
}
