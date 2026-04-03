package gruppei.backend.repository;

import gruppei.backend.database.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Integer> {

    Test findTestById(int testId);

    List<Test> findAllByLvIdAndTestArt(int lvId, Test.testArtEnum testArtEnum);

    Test findByLvIdAndName(int lvId, String name);

    Test findTestByLvIdAndTestArt(int lvID, Test.testArtEnum testArtEnum);

}
