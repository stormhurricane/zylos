package gruppei.backend.repository;

import gruppei.backend.database.Frage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FrageRepository extends JpaRepository<Frage, Integer> {

    List<Frage> findAllByTestId(int testId);

    Frage findById(int id);

}
