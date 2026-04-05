package com.zylos.backend.controller.communication;

import java.util.List;

import com.zylos.backend.database.ChatNachricht;

public class ChatWrapper {

    private int chatId;
    private List<ChatNachricht> chatNachrichten;

    public ChatWrapper(int chatId, List<ChatNachricht> chatNachrichten) {
        this.chatId = chatId;
        this.chatNachrichten = chatNachrichten;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public List<ChatNachricht> getChatNachrichten() {
        return chatNachrichten;
    }

    public void setChatNachrichten(List<ChatNachricht> chatNachrichten) {
        this.chatNachrichten = chatNachrichten;
    }
}
