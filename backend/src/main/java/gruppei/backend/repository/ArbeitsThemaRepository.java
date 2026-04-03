package gruppei.backend.repository;

import gruppei.backend.database.ArbeitsThema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArbeitsThemaRepository extends JpaRepository<ArbeitsThema, Integer> {

    List<ArbeitsThema> findAllByLehrendenId(int lehrendenId);

}
