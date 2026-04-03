package gruppei.backend.repository;

import gruppei.backend.database.Lehrender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LehrenderRepository extends JpaRepository<Lehrender, Integer> {

        Lehrender findLehrenderByEmail(String email);

}
