package gruppei.backend.repository;

import gruppei.backend.database.ToDos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDosRepository extends JpaRepository<ToDos, Integer> {

    List<ToDos> findAllByProjektgruppenId(int projektgruppenId);

    ToDos findById(int id);
}
