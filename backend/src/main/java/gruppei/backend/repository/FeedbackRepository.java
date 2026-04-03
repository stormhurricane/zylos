package gruppei.backend.repository;

import gruppei.backend.database.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    List<Feedback> findAllByVersuchId(int versuchId);

    List<Feedback> findAllByFrageId(int frageId);

}
