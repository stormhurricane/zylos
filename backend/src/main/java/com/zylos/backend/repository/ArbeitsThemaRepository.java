package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zylos.backend.database.ArbeitsThema;

import java.util.List;

public interface ArbeitsThemaRepository extends JpaRepository<ArbeitsThema, Integer> {

    List<ArbeitsThema> findAllByLehrendenId(int lehrendenId);

}
