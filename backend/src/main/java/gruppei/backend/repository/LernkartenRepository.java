package gruppei.backend.repository;

import gruppei.backend.database.Lehrveranstaltung;
import gruppei.backend.database.Lernkarte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LernkartenRepository  extends JpaRepository<Lernkarte, Integer> {

    List<Lernkarte> findAllByLernkartenThemaId(int Id);
}
