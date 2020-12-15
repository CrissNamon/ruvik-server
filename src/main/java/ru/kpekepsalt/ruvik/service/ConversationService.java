package ru.kpekepsalt.ruvik.service;

import ru.kpekepsalt.ruvik.model.Conversation;
import ru.kpekepsalt.ruvik.model.ConversationStatus;

import java.util.List;

public interface ConversationService {

    void save(Conversation conversation);
    Conversation findBySession(String sessionKey);
    List<Conversation> findByReceiverId(Long id);
    List<Conversation> findBySenderId(Long id);
    List<Conversation> findByStatusAndReceiverId(ConversationStatus status, Long id);
    List<Conversation> findByStatusAndSenderId(ConversationStatus status, Long id);
    Conversation findByReceiverIdAndSenderId(Long receiver, Long sender);
    Conversation findById(Long id);
    void establishSession(Long id);
}
