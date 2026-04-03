package gruppei.backend.repository;

import gruppei.backend.database.ZFA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZFARepository extends JpaRepository<ZFA, Integer> {

    ZFA findById(int id);

}
