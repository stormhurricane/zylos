package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zylos.backend.database.Frage;

import java.util.List;

@Deprecated(since = "2024-06", forRemoval = true)
public interface FrageRepository extends JpaRepository<Frage, Integer> {

    List<Frage> findAllByTestId(int testId);

    Frage findById(int id);

}
