package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.Lehrveranstaltung;

import java.util.List;

@Repository
public interface LehrveranstaltungsRepository  extends JpaRepository<Lehrveranstaltung, Integer> {

    Lehrveranstaltung findLehrveranstaltungByLehrveranstaltungsID(int id);

    Lehrveranstaltung findLehrveranstaltungByTitelAndSemesterZeitAndSemesterJahrAndTyp(String titel,
                                                                          Lehrveranstaltung.zeitEnum semesterZeit,
                                                                String semesterJahr, Lehrveranstaltung.typEnum typ);

    List<Lehrveranstaltung> findAllBySemesterJahrAndAndSemesterZeit(String semesterJahr, Lehrveranstaltung.zeitEnum semesterZeit);

}

