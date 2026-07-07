package com.zylos.backend.database;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "CHATNACHRICHT")
@Deprecated(since = "2024-06", forRemoval = true)
public class ChatNachricht {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column
    private long id;

    private int chatId;

    private String sender;

    private String inhalt;

    public ChatNachricht(int chatId, String sender, String inhalt) {
        this.chatId = chatId;
        this.sender = sender;
        this.inhalt = inhalt;
    }

    public ChatNachricht() {

    }

    public long getId() {
        return id;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }
}
