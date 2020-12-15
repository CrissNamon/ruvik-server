package ru.kpekepsalt.ruvik.service;

import ru.kpekepsalt.ruvik.model.Conversation;
import ru.kpekepsalt.ruvik.model.Message;

import java.util.List;

public interface MessageService {
    void save(Message message);
    Conversation haveConversation(Long id);
    List<Message> findByIdGreaterThan(Long conversationId, Long id);

}
