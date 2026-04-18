package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.zylos.backend.controller.communication.VeranstaltungsWrapper;
import com.zylos.backend.database.*;
import com.zylos.backend.model.dto.CourseSearchRequest;
import com.zylos.backend.repository.LehrveranstaltungsRepository;

import java.util.*;

@Service
public class LehrveranstaltungsService {

    @Autowired
    @Lazy
    TeilnehmerService teilnehmerService;

    @Autowired
    ProjektgruppeService projektgruppeService;

    @Autowired
    LehrveranstaltungsRepository lehrveranstaltungsRepository;

    public List<VeranstaltungsWrapper> erzeugeLehrveranstaltungsListe() {
        List<Lehrveranstaltung> lehrveranstaltungsListe;
        lehrveranstaltungsListe = lehrveranstaltungsRepository.findAll();
//        lehrveranstaltungsListe.addAll(projektgruppeService.zeigeAlleProjektGruppen());

        lehrveranstaltungsListe.sort(new Comparator<Lehrveranstaltung>() {
            // Semesterbenennung wie folgt: SS2020 -> WS2020/2021 -> SS2021 -> WS2021/2022 -> ...
            // je weiter links ein Element in der Liste ist, desto weiter oben wird es angezeigt
            @Override
            public int compare(Lehrveranstaltung lv1, Lehrveranstaltung lv2) {
                int lv1Jahr = LehrveranstaltungsService.bestimmeLVJahr(lv1.getSemesterJahr());
                int lv2Jahr = LehrveranstaltungsService.bestimmeLVJahr(lv2.getSemesterJahr());
                if (lv1Jahr < lv2Jahr) {
                    return 1;
                } else if (lv1Jahr > lv2Jahr) {
                    return -1;
                } else if (lv1.getSemesterZeit() == Lehrveranstaltung.zeitEnum.SS
                        && lv2.getSemesterZeit() == Lehrveranstaltung.zeitEnum.WS) {
                    return -1;
                } else if (lv1.getSemesterZeit() == Lehrveranstaltung.zeitEnum.WS
                        && lv2.getSemesterZeit() == Lehrveranstaltung.zeitEnum.SS)
                {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        List<VeranstaltungsWrapper> veranstaltungen = new ArrayList<>();
        for (Lehrveranstaltung lv : lehrveranstaltungsListe) {
            if (lv instanceof Projektgruppe) {
                veranstaltungen.add(new VeranstaltungsWrapper((Projektgruppe) lv));
            }
            else {
                veranstaltungen.add(new VeranstaltungsWrapper(lv));
            }
        }
        return veranstaltungen;
    }

 
    public VeranstaltungsWrapper findeLehrveranstaltung(CourseSearchRequest request) {
        String titel = request.title();
        String semesterZeit = request.semesterTime().toUpperCase();
        String semesterJahr = request.semesterYear();
        String typ = request.type();
        Lehrveranstaltung.zeitEnum zE = Lehrveranstaltung.zeitEnum.valueOf(semesterZeit);
        if(typ != null) {
            typ = typ.toUpperCase();
            Lehrveranstaltung.typEnum tE= Lehrveranstaltung.typEnum.valueOf(typ);
            Lehrveranstaltung lv = lehrveranstaltungsRepository
                    .findLehrveranstaltungByTitelAndSemesterZeitAndSemesterJahrAndTyp(titel, zE, semesterJahr, tE);

            if (lv != null) {
                return new VeranstaltungsWrapper(lv);
        } else {
                return null;
          }

        } else {
            Projektgruppe pg = projektgruppeService.findeProjektGruppe(titel, zE, semesterJahr);
            if (pg != null) {
                return new VeranstaltungsWrapper(pg);
            }
            return null;
        }
    }

    public static int bestimmeLVJahr(String semester) {

        char[] semesterArray = semester.toCharArray();
        return Character.getNumericValue(semesterArray[semesterArray.length - 2]) * 10
                + Character.getNumericValue(semesterArray[semesterArray.length - 1]);
    }

    public boolean bestimmeKorrekteJahreseingabe(String semester) {
        if (semester.length() == 4) {
            if (semester.matches("[0-9]+")) {
                return true;
            } } else {
                if (semester.substring(0, 4).matches("[0-9]+") && semester.substring(5, 9).matches("[0-9]+")) {
                    return true;
                }
            }
        return false;
    }

    public List<Lehrveranstaltung> findeLehrveranstaltungen(Lehrveranstaltung.zeitEnum semesterZeit, String semesterJahr) {
        return lehrveranstaltungsRepository.findAllBySemesterJahrAndAndSemesterZeit(semesterJahr, semesterZeit);
    }
}
