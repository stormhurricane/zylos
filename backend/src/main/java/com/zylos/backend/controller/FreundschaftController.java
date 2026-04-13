package com.zylos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.model.dto.FriendshipDecisionRequest;
import com.zylos.backend.model.dto.NutzerResponse;
import com.zylos.backend.service.FreundschaftService;

import java.util.List;

@RestController
@RequestMapping(path="api/v2/friends")
public class FreundschaftController {

    // TODO methoden in NutzerController integrieren, im sinne /users/{id}/friends

    @Autowired
    FreundschaftService freundschaftService;

    @GetMapping(path="/show/{id}")
    public List<NutzerResponse> showFriendsOfUser(@PathVariable("id") int userId) {
        return freundschaftService.showFriendsOfUser(userId);
    }

    //true, falls bereits oder nun Freunde, sonst false
    @PostMapping(path="/sendRequest/{id}")
    public boolean sendeAnfrage(@PathVariable("id") int nutzerId1,
                                 @RequestBody int nutzerId2) {
       return freundschaftService.sendeFreundschaftsAnfrage(nutzerId1, nutzerId2);
    }

    @PostMapping(path="/respond")
    public boolean bearbeiteFreundschaftsAnfrage(@RequestBody FriendshipDecisionRequest request) {
        return freundschaftService.schliesseFreundschaft(request.userIds(), request.accepted());
    }
    

    @GetMapping(path="/openRequests/{id}")
    public List<NutzerResponse> showOpenFriendRequests(@PathVariable("id") int userId) {
        return freundschaftService.showOpenFriendRequests(userId);
    }
}
