package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.LernkartenThema;

import java.util.List;

@Repository
public interface LernkartenThemaRepository  extends JpaRepository<LernkartenThema, Integer> {

    LernkartenThema findByLvIdAndBeschreibung(int lvId, String beschreibung);

    List<LernkartenThema> findAllByLvId(int lvId);
}
