package ru.kpekepsalt.ruvik.dto;

import ru.kpekepsalt.ruvik.enums.ConversationStatus;

public class ConversationDto{

    private Long conversationId;
    private String sessionKey;
    private Long receiverId;
    private Long senderId;
    private ConversationStatus status;
    private String oneTimeKey;

    public ConversationDto() {}

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
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

    public void setOneTimeKey(String oneTimeKey) {
        this.oneTimeKey = oneTimeKey;
    }
}
