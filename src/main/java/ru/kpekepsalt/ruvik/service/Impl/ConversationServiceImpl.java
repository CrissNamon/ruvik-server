package ru.kpekepsalt.ruvik.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpekepsalt.ruvik.model.Conversation;
import ru.kpekepsalt.ruvik.model.ConversationStatus;
import ru.kpekepsalt.ruvik.repository.ConversationRepository;
import ru.kpekepsalt.ruvik.service.ConversationService;

import java.util.List;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public void save(Conversation conversation) {
        conversationRepository.save(conversation);
    }

    @Override
    public Conversation findBySession(String sessionKey) {
        return conversationRepository.findBySessionKey(sessionKey).orElse(null);
    }

    @Override
    public List<Conversation> findByReceiverId(Long id) {
        return conversationRepository.findByReceiverId(id);
    }

    @Override
    public List<Conversation> findBySenderId(Long id) {
        return conversationRepository.findBySenderId(id);
    }

    @Override
    public List<Conversation> findByStatusAndReceiverId(ConversationStatus status, Long id) {
        return conversationRepository.findByStatusAndReceiverId(status, id);
    }

    @Override
    public List<Conversation> findByStatusAndSenderId(ConversationStatus status, Long id) {
        return conversationRepository.findByStatusAndSenderId(status, id);
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
}
