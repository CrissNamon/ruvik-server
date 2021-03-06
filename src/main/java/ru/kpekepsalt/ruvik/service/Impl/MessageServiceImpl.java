package ru.kpekepsalt.ruvik.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpekepsalt.ruvik.model.Conversation;
import ru.kpekepsalt.ruvik.model.Message;
import ru.kpekepsalt.ruvik.model.User;
import ru.kpekepsalt.ruvik.repository.ConversationRepository;
import ru.kpekepsalt.ruvik.repository.MessageRepository;
import ru.kpekepsalt.ruvik.service.ConversationService;
import ru.kpekepsalt.ruvik.service.MessageService;
import ru.kpekepsalt.ruvik.service.UserService;

import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Override
    public Conversation haveConversation(Long id) {
        Conversation conversation = conversationService.findById(id);
        return conversation;
    }

    @Override
    public List<Message> findByIdGreaterThan(Long conversationId, Long id) {
        return messageRepository.findByConversationIdAndIdGreaterThan(
                conversationId,
                id);
    }
}
