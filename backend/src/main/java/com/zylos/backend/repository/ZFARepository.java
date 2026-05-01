package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zylos.backend.database.ZFA;

@Deprecated(since = "2026-04", forRemoval = true)
public interface ZFARepository extends JpaRepository<ZFA, Integer> {

    ZFA findById(int id);

}
