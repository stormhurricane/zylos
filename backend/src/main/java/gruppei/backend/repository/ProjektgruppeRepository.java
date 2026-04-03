package gruppei.backend.repository;

import gruppei.backend.database.Lehrveranstaltung;
import gruppei.backend.database.Projektgruppe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjektgruppeRepository extends JpaRepository<Projektgruppe, Integer> {

    Projektgruppe findProjektgruppeByLehrveranstaltungsID(int id);

    Projektgruppe findProjektgruppeByTitelAndSemesterZeitAndSemesterJahr(String titel,
                                                                         Lehrveranstaltung.zeitEnum semesterZeit,
                                                                         String semesterJahr);
}
