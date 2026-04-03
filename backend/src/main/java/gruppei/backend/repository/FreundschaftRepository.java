package gruppei.backend.repository;

import gruppei.backend.database.Freundschaft;
import gruppei.backend.database.FreundesListeID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreundschaftRepository extends JpaRepository<Freundschaft, FreundesListeID> {
    Freundschaft findByFreundesListeID(FreundesListeID freundesListeID);

    List<Freundschaft> findByFreundesListeID_NutzerId1(int nutzerId1);
    List<Freundschaft> findByFreundesListeID_NutzerId2(int nutzerId2);
}
