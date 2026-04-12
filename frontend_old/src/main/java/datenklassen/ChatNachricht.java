package datenklassen;

public class ChatNachricht {

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
