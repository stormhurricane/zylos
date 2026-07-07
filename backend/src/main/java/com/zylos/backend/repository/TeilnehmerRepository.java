package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.ListID;
import com.zylos.backend.database.Teilnehmer;

@Repository
@Deprecated(since = "2024-06", forRemoval = true)
public interface TeilnehmerRepository extends JpaRepository<Teilnehmer, ListID> {
}
