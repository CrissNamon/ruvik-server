package ru.kpekepsalt.ruvik.service;

import ru.kpekepsalt.ruvik.dto.ConversationDto;
import ru.kpekepsalt.ruvik.dto.SessionInitialInformationDto;
import ru.kpekepsalt.ruvik.exception.DataValidityException;
import ru.kpekepsalt.ruvik.functional.VoidActionFunctional;
import ru.kpekepsalt.ruvik.functional.VoidParamActionFunctional;
import ru.kpekepsalt.ruvik.model.Conversation;
import ru.kpekepsalt.ruvik.enums.ConversationStatus;

import java.util.List;

public interface ConversationService {

    /**
     * Saves conversation data
     * @param conversation Conversation to save
     * @return Saved conversation
     */
    Conversation save(Conversation conversation) throws DataValidityException;

    /**
     * Search conversation by session key
     * @param sessionKey Conversation session key
     * @return Found conversation or null if not exists
     */
    Conversation findBySession(String sessionKey);

    /**
     * Search conversations by status and receiver id
     * @param status Conversation status
     * @param id Receiver id
     * @return List of found conversation
     */
    List<Conversation> findByStatusAndReceiverId(ConversationStatus status, Long id);

    /**
     * Search conversation between receiver and sender
     * @param receiver Receiver id
     * @param sender Sender id
     * @return Found conversation or null if not exists
     */
    Conversation findByReceiverIdAndSenderId(Long receiver, Long sender);

    /**
     * Search conversation by id
     * @param id Conversation id
     * @return Found conversation or null if not exists
     */
    Conversation findById(Long id);

    /**
     * Establish session of conversation with given id
     * @param id Conversation id
     */
    void establishSession(Long id);

    /**
     * Initiates conversation with receiver
     * @param login Receiver login
     * @param sessionInitialInformationDto Session information data
     * @param userNotFound Action if user won't be found
     * @param alreadyInitiated Action if conversation has been already initiated
     * @param onSuccess Action if conversation successfully initiated
     */
    void initiate(String login, SessionInitialInformationDto sessionInitialInformationDto,
                  VoidActionFunctional userNotFound,
                  VoidActionFunctional alreadyInitiated,
                  VoidParamActionFunctional<ConversationDto> onSuccess) throws DataValidityException;
}
