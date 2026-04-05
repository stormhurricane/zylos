package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.ToDos;

import java.util.List;

@Repository
public interface ToDosRepository extends JpaRepository<ToDos, Integer> {

    List<ToDos> findAllByProjektgruppenId(int projektgruppenId);

    ToDos findById(int id);
}
