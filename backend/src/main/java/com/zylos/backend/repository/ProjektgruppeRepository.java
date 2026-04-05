package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.Lehrveranstaltung;
import com.zylos.backend.database.Projektgruppe;

@Repository
public interface ProjektgruppeRepository extends JpaRepository<Projektgruppe, Integer> {

    Projektgruppe findProjektgruppeByLehrveranstaltungsID(int id);

    Projektgruppe findProjektgruppeByTitelAndSemesterZeitAndSemesterJahr(String titel,
                                                                         Lehrveranstaltung.zeitEnum semesterZeit,
                                                                         String semesterJahr);
}
