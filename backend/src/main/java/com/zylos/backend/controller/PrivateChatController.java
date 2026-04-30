package com.zylos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.controller.communication.ChatWrapper;
import com.zylos.backend.database.Chat;
import com.zylos.backend.database.ChatNachricht;
import com.zylos.backend.service.ChatService;

import java.util.List;

@RestController
@RequestMapping(path="api/v2/privatechat")
@Deprecated(since = "2026-04", forRemoval = true)
public class PrivateChatController {

    @Autowired
    ChatService chatService;

    //gibt bei verschicken einer Nachricht die geupdateten Chat zurück.
    @PostMapping(path="/send")
    public List<ChatNachricht> sendePrivateNachricht(@RequestBody ChatNachricht nachricht) {
        return chatService.erstelleNachricht(nachricht);
    }

    //ChatWrapper dient zur Übergabe von ChatID und aller Nachrichten
    //Nachrichten können null sein
    @PostMapping(path="/with/{id}")
    public ChatWrapper zeigePrivatenChatAn(@PathVariable("id") int nutzerId1,
                                           @RequestBody int nutzerId2) {
        return chatService.rufeChatAuf(nutzerId1, nutzerId2);
    }

    @GetMapping(path="/show/{id}")
    public List<Chat> zeigeChatsEinesNutzers(@PathVariable("id") int nutzerId) {
        return chatService.zeigeAlleChatsEinesNutzers(nutzerId);
    }

}
