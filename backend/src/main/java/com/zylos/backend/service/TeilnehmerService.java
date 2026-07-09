package com.zylos.backend.service;

import org.springframework.stereotype.Service;


@Service
@Deprecated(since="2024-06", forRemoval=true)
public class TeilnehmerService {


    public boolean pruefeGemeinsameLehrveranstaltung(int studentenId, int lehrendenId){

        // for(Object studentenVeranstaltung: alleLVdesStudenten){
        //     if(studentenVeranstaltung instanceof Lehrveranstaltung){
        //         Lehrveranstaltung lv = (Lehrveranstaltung) studentenVeranstaltung;
        //     } else {
        //         continue;
        //     }
        //     int studentenLvId = studentenVeranstaltung.getLehrveranstaltung().getLehrveranstaltungsID();

        //     for(Object lehrendenVeranstaltungen: alleLVdesLehrenden){
        //         if(lehrendenVeranstaltungen instanceof Lehrveranstaltung){
        //             Lehrveranstaltung lv = (Lehrveranstaltung) lehrendenVeranstaltungen;
        //         } else {
        //             continue;
        //         }
        //         if(studentenLvId == lehrendenVeranstaltungen.getLehrveranstaltung().getLehrveranstaltungsID()){
        //             return true;
        //         }
        //     }
        // }
        return false;
    }
}
