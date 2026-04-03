package gruppei.backend.controller;

import gruppei.backend.database.LehrveranstaltungsMaterial;
import gruppei.backend.service.LehrveranstaltungsMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/lehrveranstaltungmaterial")
public class LehrveranstaltungsMaterialController {

    @Autowired
    LehrveranstaltungsMaterialService lehrveranstaltungsMaterialService;

    @GetMapping(path = "/find/{id}", produces = "application/json")
    public List<LehrveranstaltungsMaterial> findeLehrveranstaltungsMaterialEinerLV(@PathVariable("id") int id) {
        return lehrveranstaltungsMaterialService.erstelleMaterialListe(id);
    }

    //im Frontend wird die lvId in Material gemapped
    @PostMapping(path = "/hinzufuegen")
    public boolean fuegeMaterialHinzu(@RequestBody LehrveranstaltungsMaterial lehrveranstaltungsMaterial) {
        return lehrveranstaltungsMaterialService.fuegeMaterialHinzu(lehrveranstaltungsMaterial);
    }

}
