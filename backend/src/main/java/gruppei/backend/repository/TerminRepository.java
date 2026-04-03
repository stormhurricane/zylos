package gruppei.backend.repository;

import gruppei.backend.database.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerminRepository extends JpaRepository<Termin, Integer> {
    List<Termin> findAllByLvIdAndJahrAndMonatAndTag (int lvId, String jahr, String monat, String tag);

    Termin findById (int id);

    List<Termin> findAllByLvId(int lvid);
}
