package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zylos.backend.database.Versuch;

import java.util.List;

@Deprecated(since = "2026-04", forRemoval = true)
public interface VersuchRepository extends JpaRepository<Versuch, Integer> {

    Versuch findVersuchById(int id);

    List<Versuch> findAllByTestId(int testId);

    List<Versuch> findAllByNutzerIdAndTestId(int nutzerId, int testId);

    List<Versuch> findAllByNutzerIdAndTestIdAndBestanden(int nutzerId, int testId, boolean bestanden);

    Versuch findVersuchByNutzerIdAndTestId(int nutzerId, int testId);

}
