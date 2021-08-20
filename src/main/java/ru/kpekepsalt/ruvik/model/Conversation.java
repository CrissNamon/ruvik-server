package ru.kpekepsalt.ruvik.model;

import ru.kpekepsalt.ruvik.enums.ConversationStatus;

import javax.persistence.*;

@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "session_key")
    private String sessionKey;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ConversationStatus status;

    @Column(name = "onetime_key")
    private String oneTimeKey;

    public Conversation() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public ConversationStatus getStatus() {
        return status;
    }

    public void setStatus(ConversationStatus status) {
        this.status = status;
    }

    public String getOneTimeKey() {
        return oneTimeKey;
    }

    public void setOneTimeKey(String oneTImeKey) {
        this.oneTimeKey = oneTImeKey;
    }

    public Conversation copy(Conversation from) {
        Conversation to = new Conversation();
        to.setStatus(from.getStatus());
        to.setSenderId(from.getSenderId());
        to.setReceiverId(from.getReceiverId());
        to.setOneTimeKey(from.getOneTimeKey());
        to.setSessionKey(from.getSessionKey());
        return to;
    }
}
