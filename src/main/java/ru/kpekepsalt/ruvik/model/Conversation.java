package ru.kpekepsalt.ruvik.model;

import ru.kpekepsalt.ruvik.enums.ConversationStatus;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "session_key")
    @NotBlank(message = "Session key can't be empty")
    private String sessionKey;

    @Column(name = "receiver_id")
    @Min(value = 0, message = "Receiver ID must be greater than zero")
    private Long receiverId;

    @Column(name = "sender_id")
    @Min(value = 0, message = "Sender ID must be greater than zero")
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
}
