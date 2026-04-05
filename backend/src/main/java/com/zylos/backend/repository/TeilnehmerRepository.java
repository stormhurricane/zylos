package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.ListID;
import com.zylos.backend.database.Teilnehmer;

@Repository
public interface TeilnehmerRepository extends JpaRepository<Teilnehmer, ListID> {
}
