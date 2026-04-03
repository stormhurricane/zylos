package gruppei.backend.service;

import gruppei.backend.database.*;
import gruppei.backend.repository.ProjektgruppeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjektgruppeService {

    @Autowired
    ProjektgruppeRepository projektgruppeRepository;

    @Autowired
    ProjektgruppenNachrichtService projektgruppenNachrichtService;

    @Autowired
    ToDosService toDosService;

    public Projektgruppe findeProjektGruppe(String titel, Lehrveranstaltung.zeitEnum semesterZeit,
                                            String semesterJahr) {
        return projektgruppeRepository.findProjektgruppeByTitelAndSemesterZeitAndSemesterJahr
                (titel, semesterZeit, semesterJahr);
    }

    public Projektgruppe erstelleProjektgruppe(Projektgruppe lv) {
        return projektgruppeRepository.save(lv);
    }

    public Projektgruppe findeProjektGruppe(int id) {
        return projektgruppeRepository.findProjektgruppeByLehrveranstaltungsID(id);
    }

    public List<ProjektgruppenNachricht> zeigeGruppenchat(int pgId) {
        return projektgruppenNachrichtService.zeigeAlleNachrichtenEinerProjektugruppe(pgId);
    }

    public List<ProjektgruppenNachricht> sendeNachricht(ProjektgruppenNachricht nachricht) {
        return projektgruppenNachrichtService.fuegeNachrichtHinzu(nachricht.getProjektgruppenId(),
                nachricht.getSender(), nachricht.getInhalt());
    }

    public List<Projektgruppe> zeigeAlleProjektGruppen() {
        return projektgruppeRepository.findAll();
    }

    public List<ToDos> zeigeToDoListe(int pgId) {
        return toDosService.listeAllerTodosEinerProjektgruppe(pgId);
    }

    public boolean fuegeToDoHinzu(ToDos neuesToDo) {
        return toDosService.fuegeTodoHinzu(neuesToDo.getProjektgruppenId(), neuesToDo.getInhalt(),
                neuesToDo.getVerantwortlichenId());
    }

    public boolean hakeToDoAb(int id) {
        return toDosService.TodoErledigt(id);
    }

    public boolean aendereVerantwortung(int todoId, int verantwortlichenId) {
        return toDosService.aenderVerantwortlichen(todoId, verantwortlichenId);
    }
}
