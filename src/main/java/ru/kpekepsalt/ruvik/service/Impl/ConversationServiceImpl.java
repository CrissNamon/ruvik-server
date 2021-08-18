package ru.kpekepsalt.ruvik.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpekepsalt.ruvik.dto.ConversationDto;
import ru.kpekepsalt.ruvik.dto.SessionInitialInformationDto;
import ru.kpekepsalt.ruvik.functional.VoidActionFunctional;
import ru.kpekepsalt.ruvik.functional.VoidParamActionFunctional;
import ru.kpekepsalt.ruvik.model.Conversation;
import ru.kpekepsalt.ruvik.model.ConversationStatus;
import ru.kpekepsalt.ruvik.model.User;
import ru.kpekepsalt.ruvik.repository.ConversationRepository;
import ru.kpekepsalt.ruvik.service.ConversationService;
import ru.kpekepsalt.ruvik.service.UserService;

import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

/**
 * Service for conversation operations
 */
@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public Conversation save(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    @Override
    public Conversation findBySession(String sessionKey) {
        return conversationRepository.findBySessionKey(sessionKey).orElse(null);
    }

    @Override
    public List<Conversation> findByStatusAndReceiverId(ConversationStatus status, Long id) {
        return conversationRepository.findByStatusAndReceiverId(status, id);
    }

    @Override
    public Conversation findByReceiverIdAndSenderId(Long receiver, Long sender) {
        return conversationRepository.findByReceiverIdAndSenderId(receiver, sender).orElse(null);
    }

    @Override
    public Conversation findById(Long id) {
        return conversationRepository.findById(id).orElse(null);
    }

    @Override
    public void establishSession(Long id) {
        conversationRepository.establishSession(ConversationStatus.ESTABLISHED, id);
    }

    public void initiate(String login, SessionInitialInformationDto sessionInitialInformationDto,
                         VoidActionFunctional userNotFound,
                         VoidActionFunctional alreadyInitiated,
                         VoidParamActionFunctional<ConversationDto> onSuccess)
    {
        User user = userService.findByLogin(login);
        if(isEmpty(user)) {
            userNotFound.action();
            return;
        }
        Conversation conversation = findByReceiverIdAndSenderId(userDetailsService.getUserid(), user.getId());
        if(!isEmpty(conversation)) {
            alreadyInitiated.action();
            return;
        }
        conversation = new Conversation();
        conversation.setSessionKey(sessionInitialInformationDto.getEncryptedSessionKey());
        conversation.setOneTimeKey(sessionInitialInformationDto.getOneTImeKey());
        conversation.setReceiverId(user.getId());
        conversation.setSenderId(userDetailsService.getUserid());
        conversation.setStatus(ConversationStatus.PENDING);
        save(conversation);
        conversation = findBySession(conversation.getSessionKey());
        ConversationDto dto = new ConversationDto(conversation);
        onSuccess.action(dto);
    }
}
