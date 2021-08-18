package ru.kpekepsalt.ruvik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpekepsalt.ruvik.dto.*;
import ru.kpekepsalt.ruvik.model.*;
import ru.kpekepsalt.ruvik.service.ConversationService;
import ru.kpekepsalt.ruvik.service.Impl.UserDetailsServiceImpl;
import ru.kpekepsalt.ruvik.service.MessageService;
import ru.kpekepsalt.ruvik.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static ru.kpekepsalt.ruvik.Urls.API_PATH;
import static ru.kpekepsalt.ruvik.Utils.ValidationUtils.isValid;

/**
 * Controller for managing conversations
 */
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

    /**
     * @return List of pending conversations
     */
    @GetMapping("/pending")
    public ResponseEntity<Response<List<Conversation>>> getPendingConversations() {
        Long id = userDetailsService.getUserid();
        List<Conversation> pendingConversations = conversationService.findByStatusAndReceiverId(ConversationStatus.PENDING, id);
        return ResponseEntity.ok(
                new Response<>("", pendingConversations)
        );
    }

    /**
     * @param login User login to find
     * @return HTTP 200 - User information
     * HTTP 400 - Request error
     * HTTP 404 - User with given login not found
     */
    @GetMapping("/initiate/{login}")
    public ResponseEntity<Response<UserDto>> findUserByLogin(@NotBlank @Valid @PathVariable("login") String login) {
        if(isEmpty(login)) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Login required")
            );
        }
        User user = userService.findByLogin(login);
        if(isEmpty(user)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorResponse("User not found")
            );
        }
        if(user.getId().equals(userDetailsService.getUserid())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ErrorResponse("Given id belongs to you! -_-")
            );
        }
        UserDto dto = new UserDto(user);
        return ResponseEntity.ok(
                new Response<>("", dto)
        );
    }

    /**
     * @param login User login to conversation initiation
     * @param sessionInitialInformationDto Session information for initiation
     * @return HTTP 200 - Initiated conversation data
     * HTTP 400 - Request error
     * HTTP 404 - User with given login not found
     * HTTP 302 - Session has been already initiated
     */
    @PostMapping("/initiate/{login}")
    public ResponseEntity<Response<ConversationDto>> createPendingSession(@PathVariable("login") String login,
                                                                          @RequestBody SessionInitialInformationDto sessionInitialInformationDto) {
        if(isEmpty(login)) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Login required")
            );
        }
        if(!isValid(sessionInitialInformationDto)) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Session information is invalid")
            );
        }
        AtomicReference<ResponseEntity<Response<ConversationDto>>> response = new AtomicReference<>();
        conversationService.initiate(login, sessionInitialInformationDto,
                () -> response.set(
                        ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                new ErrorResponse("User not found")
                        )
                ),
                () -> response.set(
                        ResponseEntity.status(HttpStatus.FOUND).body(
                                new ErrorResponse("Session has been already initiated")
                        )
                ),
                conversation -> response.set(
                        ResponseEntity.ok(new Response<>("", conversation))
                ));
        return response.get();
    }

    /**
     * @param ids Identifiers of sessions to accept
     * @return HTTP 200 - List of accepted conversations
     */
    @PostMapping("/initiate/accept")
    public ResponseEntity<Response<List<ConversationDto>>> acceptPendingSessions(@RequestBody Long[] ids) {
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
        return ResponseEntity.ok(
                new Response<>("", accepted)
        );
    }

    /**
     * @param id Identifier of session to accept
     * @return HTTP 200 - Accepted conversation data
     * HTTP 400 - Request error
     * HTTP 202 - Session has been already established
     * HTTP 302 - COnversation already created
     */
    @PostMapping("/initiate/{id}/accept")
    public ResponseEntity<Response<ConversationDto>> acceptPendingSession(@PathVariable("id") Long id) {
        if(isEmpty(id)) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Session id required")
            );
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
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                        new ErrorResponse("Session has been already established")
                );
            }
        }else{
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new ErrorResponse("Conversation has been already created")
            );
        }
        return ResponseEntity.ok(
                new Response<>("", dto)
        );
    }

    /**
     * @param data List of conversations and messages offset
     * @return HTTP 200 - List of new messages starting from given offset
     */
    @PostMapping("/message/new")
    public ResponseEntity<Response<List<Message>>> getNewMessages(@RequestBody List<ConversationAndMessage> data) {
        List<Message> messages = new ArrayList<>();
        if(isEmpty(data)) {
            ResponseEntity.ok(new Response<>("", messages));
        }
        for(ConversationAndMessage item : data) {
            List<Message> newMsgs = messageService.findByIdGreaterThan(item.getConversationId(), item.getMessageId());
            Conversation conversation = conversationService.findById(item.getConversationId());
            Message first = newMsgs.stream().findFirst().orElse(null);
            if(first!=null) {
                User user = userService.findById(first.getUserId());
                newMsgs.forEach(message -> message.setSenderLogin(user.getLogin()));
            }else{
                continue;
            }
            if(!isEmpty(newMsgs)) {
                messages.addAll(newMsgs);
            }
        }
        return ResponseEntity.ok(new Response<>("", messages));
    }

}
