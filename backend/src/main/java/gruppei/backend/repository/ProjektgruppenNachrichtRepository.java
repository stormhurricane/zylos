package gruppei.backend.repository;

import gruppei.backend.database.ProjektgruppenNachricht;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjektgruppenNachrichtRepository extends JpaRepository<ProjektgruppenNachricht, Long> {

    List<ProjektgruppenNachricht> findAllByProjektgruppenId(int projekgruppenId);
}
