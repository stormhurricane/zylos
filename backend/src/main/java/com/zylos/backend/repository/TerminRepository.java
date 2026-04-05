package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.Termin;

import java.util.List;

@Repository
public interface TerminRepository extends JpaRepository<Termin, Integer> {
    List<Termin> findAllByLvIdAndJahrAndMonatAndTag (int lvId, String jahr, String monat, String tag);

    Termin findById (int id);

    List<Termin> findAllByLvId(int lvid);
}
