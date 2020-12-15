package ru.kpekepsalt.ruvik.model;

import ru.kpekepsalt.ruvik.dto.MessageDto;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "conversation_id")
    private Long conversationId;

    @Column(name = "sent_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime time;

    @Column(name = "user_id")
    private Long userId;

    @Transient
    private String senderLogin;

    public Message(MessageDto messageDto) {
        this.text = messageDto.getText();
        this.time = LocalDateTime.now();
    }

    public Message() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSenderLogin() {
        return senderLogin;
    }

    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
    }
}
