package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.Lehrender;

@Repository
public interface LehrenderRepository extends JpaRepository<Lehrender, Integer> {

        Lehrender findLehrenderByEmail(String email);

}
