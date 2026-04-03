package gruppei.backend.repository;

import gruppei.backend.database.Lehrveranstaltung;
import gruppei.backend.database.LernkartenThema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LernkartenThemaRepository  extends JpaRepository<LernkartenThema, Integer> {

    LernkartenThema findByLvIdAndBeschreibung(int lvId, String beschreibung);

    List<LernkartenThema> findAllByLvId(int lvId);
}
