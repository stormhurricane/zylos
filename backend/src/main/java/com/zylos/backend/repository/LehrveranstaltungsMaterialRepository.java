package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.LehrveranstaltungsMaterial;

import java.util.ArrayList;

@Repository
public interface LehrveranstaltungsMaterialRepository  extends JpaRepository<LehrveranstaltungsMaterial, Integer> {

    LehrveranstaltungsMaterial findLehrveranstaltungsMaterialByMaterialID(int id);

    ArrayList<LehrveranstaltungsMaterial> findLehrveranstaltungsMaterialsByLehrveranstaltungsId(int lehrveranstaltungsId);

}
