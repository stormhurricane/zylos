package gruppei.backend.repository;

import gruppei.backend.database.LehrveranstaltungsMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface LehrveranstaltungsMaterialRepository  extends JpaRepository<LehrveranstaltungsMaterial, Integer> {

    LehrveranstaltungsMaterial findLehrveranstaltungsMaterialByMaterialID(int id);

    ArrayList<LehrveranstaltungsMaterial> findLehrveranstaltungsMaterialsByLehrveranstaltungsId(int lehrveranstaltungsId);

}
