package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zylos.backend.database.Feedback;

import java.util.List;

@Deprecated(since = "2024-06", forRemoval = true)
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    List<Feedback> findAllByVersuchId(int versuchId);

    List<Feedback> findAllByFrageId(int frageId);

}
