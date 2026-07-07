package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.database.ChatNachricht;
import com.zylos.backend.repository.ChatNachrichtRepository;

import java.util.Comparator;
import java.util.List;

@Service
@Deprecated(since="2024-06", forRemoval=true)
public class ChatNachrichtService {

    @Autowired
    ChatNachrichtRepository chatNachrichtRepository;

    public List<ChatNachricht> fuegeNachrichtHinzu(int chatId, String sender, String inhalt){
        ChatNachricht chatNachricht = new ChatNachricht(chatId, sender, inhalt);
        chatNachrichtRepository.save(chatNachricht);
        return this.zeigeAlleNachrichtenEinesChatsAn(chatId);
    }

    public List<ChatNachricht> zeigeAlleNachrichtenEinesChatsAn(int chatId){
        List<ChatNachricht> chatNachrichten = chatNachrichtRepository.findAllByChatId(chatId);
        chatNachrichten.sort(new Comparator<ChatNachricht>() {
            @Override
            public int compare(ChatNachricht o1, ChatNachricht o2) {
                if (o1.getId() < o2.getId()) {return -1;}
                else if (o1.getId() > o2.getId()) {return 1;}
                else {return 0;}
            }
        });
        return chatNachrichten;
    }




}
