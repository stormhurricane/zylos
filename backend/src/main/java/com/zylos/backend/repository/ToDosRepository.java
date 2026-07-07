package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zylos.backend.database.ToDos;

import java.util.List;

@Repository
@Deprecated(since = "2024-06", forRemoval = true)
public interface ToDosRepository extends JpaRepository<ToDos, Integer> {

    List<ToDos> findAllByProjektgruppenId(int projektgruppenId);

    ToDos findById(int id);
}
