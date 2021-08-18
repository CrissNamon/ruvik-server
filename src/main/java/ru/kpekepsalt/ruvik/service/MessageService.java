package ru.kpekepsalt.ruvik.service;

import ru.kpekepsalt.ruvik.model.Conversation;
import ru.kpekepsalt.ruvik.model.Message;

import java.util.List;

public interface MessageService {

    /**
     * Saves message
     * @param message Message data
     */
    void save(Message message);

    /**
     * Checks if conversation with given id exists
     * @param id Conversation id
     * @return Conversation or null if not exists
     */
    Conversation haveConversation(Long id);

    /**
     * Search for messages with offset
     * @param conversationId Conversation id
     * @param id Offset message id
     * @return List of messages of given conversation with identifiers greater than given
     */
    List<Message> findByIdGreaterThan(Long conversationId, Long id);

}
