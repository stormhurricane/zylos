package gruppei.backend.repository;

import gruppei.backend.database.ListID;
import gruppei.backend.database.Teilnehmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeilnehmerRepository extends JpaRepository<Teilnehmer, ListID> {
}
