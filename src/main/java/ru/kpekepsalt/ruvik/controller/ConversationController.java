package ru.kpekepsalt.ruvik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpekepsalt.ruvik.dto.ConversationDto;
import ru.kpekepsalt.ruvik.dto.SessionInitialInformationDto;
import ru.kpekepsalt.ruvik.dto.UserDto;
import ru.kpekepsalt.ruvik.model.*;
import ru.kpekepsalt.ruvik.service.ConversationService;
import ru.kpekepsalt.ruvik.service.Impl.UserDetailsServiceImpl;
import ru.kpekepsalt.ruvik.service.MessageService;
import ru.kpekepsalt.ruvik.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static ru.kpekepsalt.ruvik.Urls.API_PATH;

@RestController
@RequestMapping(API_PATH+"/conversation")
public class ConversationController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/pending")
    public ResponseEntity<List<Conversation>> getPendingConversations() {
        Long id = userDetailsService.getUserid();
        List<Conversation> pendingConversations = conversationService.findByStatusAndReceiverId(ConversationStatus.PENDING, id);
        return ResponseEntity.ok(pendingConversations);
    }

    @GetMapping("/initiate/{login}")
    public ResponseEntity<UserDto> findUserByLogin(@PathVariable("login") String login) {
        if(isEmpty(login)) {
            return ResponseEntity.badRequest().build();
        }
        User user = userService.findByLogin(login);
        if(isEmpty(user)) {
            return ResponseEntity.notFound().build();
        }
        if(user.getId().equals(userDetailsService.getUserid())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        UserDto dto = new UserDto(user);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/initiate/{login}")
    public ResponseEntity<ConversationDto> createPendingSession(@PathVariable("login") String login, @RequestBody SessionInitialInformationDto sessionInitialInformationDto) {
        if(isEmpty(login)) {
            return ResponseEntity.badRequest().build();
        }
        AtomicReference<ResponseEntity<ConversationDto>> response = new AtomicReference<>();
        conversationService.initiate(login, sessionInitialInformationDto,
                () -> response.set(
                        ResponseEntity.notFound().build()
                ),
                () -> response.set(
                        ResponseEntity.status(HttpStatus.FOUND).build()
                ),
                conversation -> response.set(
                        ResponseEntity.ok(conversation)
                ));
        return response.get();
    }

    @PostMapping("/initiate/accept")
    public ResponseEntity<List<ConversationDto>> acceptPendingSessions(@RequestBody Long[] ids) {
        if(isEmpty(ids)) {
            return ResponseEntity.badRequest().build();
        }
        List<ConversationDto> accepted = new ArrayList<>();
        for(Long id : ids) {
            Conversation conversation = conversationService.findById(id);
            if(!isEmpty(conversation)) {
                conversationService.establishSession(conversation.getId());
            }
            ConversationDto conversationDto = new ConversationDto(conversation);
            accepted.add(conversationDto);
        }
        return ResponseEntity.ok(accepted);
    }

    @PostMapping("/initiate/{id}/accept")
    public ResponseEntity<ConversationDto> acceptPendingSession(@PathVariable("id") Long id) {
        if(isEmpty(id)) {
            return ResponseEntity.badRequest().build();
        }
        Conversation conversation = conversationService.findById(id);
        ConversationDto dto = new ConversationDto(conversation);
        if(isEmpty(conversation)) {
            if(conversation.getStatus().equals(ConversationStatus.PENDING)) {
                Conversation response = conversation.copy(conversation);
                response.setStatus(ConversationStatus.ESTABLISHED);
                response.setOneTimeKey("");
                conversationService.save(response);
            }else{
                return ResponseEntity.status(HttpStatus.ACCEPTED).build();
            }
        }else{
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/message/new")
    public ResponseEntity<List<Message>> getNewMessages(@RequestBody List<ConversationAndMessage> data) {
        List<Message> messages = new ArrayList<>();
        if(isEmpty(data)) {
            ResponseEntity.ok(messages);
        }
        for(ConversationAndMessage item : data) {
            List<Message> newMsgs = messageService.findByIdGreaterThan(item.getConversationId(), item.getMessageId());
            Conversation conversation = conversationService.findById(item.getConversationId());
            Message first = newMsgs.stream().findFirst().orElse(null);
            if(first!=null) {
                User user = userService.findById(first.getUserId());
                newMsgs.forEach(message -> {
                    message.setSenderLogin(user.getLogin());
                });
            }else{
                continue;
            }
            if(!isEmpty(newMsgs)) {
                messages.addAll(newMsgs);
            }
        }
        return ResponseEntity.ok(messages);
    }

}
