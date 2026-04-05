package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.FreundesListeID;
import com.zylos.backend.database.Freundschaft;

import java.util.List;

@Repository
public interface FreundschaftRepository extends JpaRepository<Freundschaft, FreundesListeID> {
    Freundschaft findByFreundesListeID(FreundesListeID freundesListeID);

    List<Freundschaft> findByFreundesListeID_NutzerId1(int nutzerId1);
    List<Freundschaft> findByFreundesListeID_NutzerId2(int nutzerId2);
}
