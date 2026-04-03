package gruppei.backend.repository;

import gruppei.backend.database.Versuch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VersuchRepository extends JpaRepository<Versuch, Integer> {

    Versuch findVersuchById(int id);

    List<Versuch> findAllByTestId(int testId);

    List<Versuch> findAllByNutzerIdAndTestId(int nutzerId, int testId);

    List<Versuch> findAllByNutzerIdAndTestIdAndBestanden(int nutzerId, int testId, boolean bestanden);

    Versuch findVersuchByNutzerIdAndTestId(int nutzerId, int testId);

}
