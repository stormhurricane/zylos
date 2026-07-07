package com.zylos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zylos.backend.database.Test;

import java.util.List;

@Deprecated(since = "2024-06", forRemoval = true)
public interface TestRepository extends JpaRepository<Test, Integer> {

    Test findTestById(int testId);

    List<Test> findAllByLvIdAndTestArt(int lvId, Test.testArtEnum testArtEnum);

    Test findByLvIdAndName(int lvId, String name);

    Test findTestByLvIdAndTestArt(int lvID, Test.testArtEnum testArtEnum);

}
