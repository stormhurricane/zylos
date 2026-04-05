package com.zylos.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.database.ArbeitsThema;
import com.zylos.backend.service.ArbeitsThemaService;

import java.util.List;

@RestController
@RequestMapping(path="api/v3/topic")
public class ArbeitsThemaController {

    @Autowired
    ArbeitsThemaService arbeitsThemaService;

    @PostMapping(path="/addTopic")
    public boolean erstelleArbeitsThema(@RequestBody ArbeitsThema arbeitsThema){
        return arbeitsThemaService.erstelleArbeitsThema(arbeitsThema);
    }

    @GetMapping(path="/showAllTopics/{id}")
    public List<ArbeitsThema> gibAlleArbeitsThemenEinesLehrenden(@PathVariable("id") int lehrendenId){
        return arbeitsThemaService.gibAlleArbeitsThemenEinesLehrenden(lehrendenId);
    }

}
