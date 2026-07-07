package com.zylos.backend.features.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterRepository extends JpaRepository<MatriculationCounter, String> {

    @Query(value = "SELECT next_val FROM id_sequences WHERE sequence_name = 'student_mat_seq' FOR UPDATE", nativeQuery = true)
    Long getAndLockCounter();

    @Modifying
    @Query(value = "UPDATE id_sequences SET next_val = next_val + 1 WHERE sequence_name = 'student_mat_seq'", nativeQuery = true)
    void incrementCounter();
}