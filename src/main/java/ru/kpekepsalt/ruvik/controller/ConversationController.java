package ru.kpekepsalt.ruvik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kpekepsalt.ruvik.dto.*;
import ru.kpekepsalt.ruvik.enums.ConversationStatus;
import ru.kpekepsalt.ruvik.exception.DataValidityException;
import ru.kpekepsalt.ruvik.mapper.CloneMapper;
import ru.kpekepsalt.ruvik.mapper.ConversationMapper;
import ru.kpekepsalt.ruvik.mapper.UserMapper;
import ru.kpekepsalt.ruvik.model.Conversation;
import ru.kpekepsalt.ruvik.model.Message;
import ru.kpekepsalt.ruvik.model.User;
import ru.kpekepsalt.ruvik.objects.ValidationResult;
import ru.kpekepsalt.ruvik.service.ConversationService;
import ru.kpekepsalt.ruvik.service.Impl.UserDetailsServiceImpl;
import ru.kpekepsalt.ruvik.service.MessageService;
import ru.kpekepsalt.ruvik.service.UserService;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static ru.kpekepsalt.ruvik.Urls.API_PATH;
import static ru.kpekepsalt.ruvik.Urls.CONVERSATION;
import static ru.kpekepsalt.ruvik.utils.ValidationUtils.validate;

/**
 * Controller for managing conversations
 */
@RestController
@RequestMapping(API_PATH + CONVERSATION.END_POINT)
@Validated
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
    @GetMapping(CONVERSATION.PENDING)
    public ResponseEntity<ResponseDto<List<Conversation>>> getPendingConversations() {
        Long id = userDetailsService.getUserid();
        List<Conversation> pendingConversations = conversationService.findByStatusAndReceiverId(ConversationStatus.PENDING, id);
        return ResponseEntity.ok(
                new ResponseDto<>("", pendingConversations)
        );
    }

    /**
     * @param login User login to find
     * @return HTTP 200 - User information
     * HTTP 400 - Request error
     * HTTP 404 - User with given login not found
     */
    @GetMapping(CONVERSATION.INITIATE + "/{login}")
    public ResponseEntity<ResponseDto<UserDto>> findUserByLogin(
            @PathVariable("login") @NotBlank(message = "User login can't be null") String login) {
        if(isEmpty(login)) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponseDto<>("Login required")
            );
        }
        User user = userService.findByLogin(login);
        if(isEmpty(user)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ErrorResponseDto<>("User not found")
            );
        }
        if(user.getId().equals(userDetailsService.getUserid())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ErrorResponseDto<>("Given id belongs to you! -_-")
            );
        }
        UserDto dto = UserMapper.INSTANCE.userToDto(user);
        return ResponseEntity.ok(
                new ResponseDto<>("", dto)
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
    @PostMapping(CONVERSATION.INITIATE + "/{login}")
    public ResponseEntity<ResponseDto<ConversationDto>>
    createPendingSession(@PathVariable("login") @NotBlank(message = "User login can't be null") String login,
                         @RequestBody SessionInitialInformationDto sessionInitialInformationDto) {
        if (isEmpty(login)) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponseDto<>("Login required")
            );
        }
        ValidationResult validationResult = validate(sessionInitialInformationDto);
        if (!validationResult.isValid()) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponseDto<>(validationResult.getFirstErrorMessage())
            );
        }
        AtomicReference<ResponseEntity<ResponseDto<ConversationDto>>> response = new AtomicReference<>();
        try {
            conversationService.initiate(login, sessionInitialInformationDto,
                    () -> response.set(
                            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                    new ErrorResponseDto<>("User not found")
                            )
                    ),
                    () -> response.set(
                            ResponseEntity.status(HttpStatus.FOUND).body(
                                    new ErrorResponseDto<>("Session has been already initiated")
                            )
                    ),
                    conversation -> response.set(
                            ResponseEntity.ok(new ResponseDto<>("", conversation))
                    ));
        } catch (DataValidityException e) {
            response.set(
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ErrorResponseDto<>(e.getMessage())
                    )
            );
        }
        return response.get();
    }

    /**
     * @param ids Identifiers of sessions to accept
     * @return HTTP 200 - List of accepted conversations
     */
    @PostMapping(CONVERSATION.INITIATE + CONVERSATION.ACCEPT)
    public ResponseEntity<ResponseDto<List<ConversationDto>>> acceptPendingSessions(
            @RequestBody @NotEmpty(message = "List can't be null") Long[] ids) {
        if(isEmpty(ids)) {
            return ResponseEntity.badRequest().build();
        }
        List<ConversationDto> accepted = new ArrayList<>();
        for(Long id : ids) {
            Conversation conversation = conversationService.findById(id);
            if(!isEmpty(conversation)) {
                conversationService.establishSession(conversation.getId());
            }
            ConversationDto conversationDto = ConversationMapper.INSTANCE.conversationToDto(conversation);
            accepted.add(conversationDto);
        }
        return ResponseEntity.ok(
                new ResponseDto<>("", accepted)
        );
    }

    /**
     * @param id Identifier of session to accept
     * @return HTTP 200 - Accepted conversation data
     * HTTP 400 - Request error
     * HTTP 202 - Session has been already established
     * HTTP 302 - COnversation already created
     */
    @PostMapping(CONVERSATION.INITIATE + "/{id}" + CONVERSATION.ACCEPT)
    public ResponseEntity<ResponseDto<ConversationDto>> acceptPendingSession(
            @PathVariable("id") @Min(value = 0, message = "Id must be greater than 0") Long id) throws DataValidityException {
        if(isEmpty(id)) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponseDto<>("Session id required")
            );
        }
        Conversation conversation = conversationService.findById(id);
        ConversationDto dto = ConversationMapper.INSTANCE.conversationToDto(conversation);
        if(isEmpty(conversation)) {
            if(conversation.getStatus().equals(ConversationStatus.PENDING)) {
                Conversation response = CloneMapper.INSTANCE.cloneConversation(conversation);
                response.setStatus(ConversationStatus.ESTABLISHED);
                response.setOneTimeKey("");
                conversationService.save(conversation);
            }else{
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                        new ErrorResponseDto<>("Session has been already established")
                );
            }
        }else{
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new ErrorResponseDto<>("Conversation has been already created")
            );
        }
        return ResponseEntity.ok(
                new ResponseDto<>("", dto)
        );
    }

    /**
     * @param data List of conversations and messages offset
     * @return HTTP 200 - List of new messages starting from given offset
     */
    @PostMapping(CONVERSATION.MESSAGE + CONVERSATION.NEW)
    public ResponseEntity<ResponseDto<List<Message>>> getNewMessages(
            @RequestBody @NotEmpty(message = "List can't be null") List<ConversationAndMessageDto> data) {
        List<Message> messages = new ArrayList<>();
        if(isEmpty(data)) {
            ResponseEntity.ok(new ResponseDto<>("", messages));
        }
        for(ConversationAndMessageDto item : data) {
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
        return ResponseEntity.ok(new ResponseDto<>("", messages));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto<Exception>> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorResponseDto<>(e.getMessage())
                );
    }

    @ExceptionHandler(DataValidityException.class)
    public ResponseEntity<ErrorResponseDto<Exception>> handleDataValidityException(DataValidityException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponseDto<>(e.getMessage())
                );
    }

}
