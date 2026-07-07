package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.ProjektgruppenNachricht;

import java.util.List;

@Repository
@Deprecated(since = "2024-06", forRemoval = true)
public interface ProjektgruppenNachrichtRepository extends JpaRepository<ProjektgruppenNachricht, Long> {

    List<ProjektgruppenNachricht> findAllByProjektgruppenId(int projekgruppenId);
}
