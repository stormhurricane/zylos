package gruppei.backend.repository;


import gruppei.backend.database.BewertungsFeedback;
import gruppei.backend.database.Lehrender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BewertungsFeedbackRepository extends JpaRepository<BewertungsFeedback, Integer> {

    List<BewertungsFeedback> findAllByVersuchId(int versuchId);

    List<BewertungsFeedback> findAllByFrageId(int frageId);
}
