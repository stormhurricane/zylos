package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.controller.communication.ChatWrapper;
import com.zylos.backend.database.Chat;
import com.zylos.backend.database.ChatNachricht;
import com.zylos.backend.repository.ChatRepository;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    ChatNachrichtService chatNachrichtService;

    @Autowired
    ChatRepository chatRepository;

    public boolean erstelleChat(int nutzerId1, int nutzerId2){
        Chat chat = new Chat(nutzerId1, nutzerId2);
        if(chatRepository.findChatByNutzerId1AndNutzerId2(nutzerId1, nutzerId2) == null &&
           chatRepository.findChatByNutzerId1AndNutzerId2(nutzerId2, nutzerId1) == null){
            chatRepository.save(chat);
            return true;
        } else {
            return false;
        }
    }


    public List<Chat> zeigeAlleChatsEinesNutzers(int nutzerId){
        List<Chat> chats = chatRepository.findAllByNutzerId1(nutzerId);
        chats.addAll(chatRepository.findAllByNutzerId2(nutzerId));
        return chats;
    }

    //Überprüft, ob möglicher Chat existiert, und gibt ChatID zurück
    //wenn nicht, wird ein neuer Chat erstellt.
    public int sucheChat(int nutzerId1, int nutzerId2) {
        Chat moeglicherChat = chatRepository.findChatByNutzerId1AndNutzerId2(nutzerId1, nutzerId2);
        if (moeglicherChat != null) {
            return moeglicherChat.getId();
        }
        else {
            moeglicherChat = chatRepository.findChatByNutzerId1AndNutzerId2(nutzerId2, nutzerId1);
            if (moeglicherChat != null) {
                return moeglicherChat.getId();
            }
            else {
                Chat neuerChat = new Chat(nutzerId1, nutzerId2);
                return chatRepository.save(neuerChat).getId();
            }
        }
    }

    public ChatWrapper rufeChatAuf(int nutzerId1, int nutzerId2) {
        int chatId = this.sucheChat(nutzerId1, nutzerId2);
        List<ChatNachricht> chatNachrichten = chatNachrichtService.zeigeAlleNachrichtenEinesChatsAn(chatId);
        return new ChatWrapper(chatId, chatNachrichten);
    }


    public List<ChatNachricht> erstelleNachricht(ChatNachricht nachricht) {
        return chatNachrichtService.fuegeNachrichtHinzu(nachricht.getChatId(),
                nachricht.getSender(), nachricht.getInhalt());
    }
}
