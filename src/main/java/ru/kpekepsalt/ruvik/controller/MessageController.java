package ru.kpekepsalt.ruvik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.kpekepsalt.ruvik.dto.*;
import ru.kpekepsalt.ruvik.functional.Functional;
import ru.kpekepsalt.ruvik.functional.VoidActionFunctional;
import ru.kpekepsalt.ruvik.functional.VoidParamActionFunctional;
import ru.kpekepsalt.ruvik.functional.VoidParamsActionFunctional;
import ru.kpekepsalt.ruvik.model.Conversation;
import ru.kpekepsalt.ruvik.model.Message;
import ru.kpekepsalt.ruvik.model.User;
import ru.kpekepsalt.ruvik.service.Impl.UserDetailsServiceImpl;
import ru.kpekepsalt.ruvik.service.MessageService;
import ru.kpekepsalt.ruvik.service.UserService;

import java.time.LocalDateTime;
import java.util.Map;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@RestController
public class MessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserService userService;

    @MessageMapping("/send")
    public void processMessage(@Payload NetworkMessageDto networkMessageDto, @Header("Authorization") String token) {
        System.out.println("STOMP MESSAGE RECEIVED");
        User user = userService.findByToken(token);
        if(isEmpty(user)) {
            return;
        }
        NetworkAction action = networkMessageDto.getNetworkAction();
        switch(action) {
            case RESPONSE:
                break;
            case SEND_MESSAGE:
                networkMessageDto.getData().setUserId(user.getId());
                sendMessage(
                        networkMessageDto.getData(),
                        (message, conversation) -> {
                            Long localId = networkMessageDto.getData().getLocalId();
                            MessageDto sentMessage = new MessageDto();
                            sentMessage.setMessageId(message.getId());
                            sentMessage.setConversationId(message.getConversationId());
                            sentMessage.setText(message.getText());
                            sentMessage.setUserId(networkMessageDto.getData().getUserId());
                            sentMessage.setSenderLogin(networkMessageDto.getData().getSenderLogin());
                            sentMessage.setTime(LocalDateTime.now());
                            Long toId = conversation.getReceiverId();
                            if(toId.equals(user.getId())) {
                                toId = conversation.getSenderId();
                            }
                            simpMessagingTemplate.convertAndSendToUser(
                                    toId.toString(), "/queue/messages",
                                    createNetworkMessage(NetworkStatus.SUCCESS, sentMessage)
                            );
                            simpMessagingTemplate.convertAndSendToUser(
                                    toId.toString(), "/queue/notifications",
                                    createNetworkMessage(NetworkStatus.SUCCESS, NetworkAction.NEW_MESSAGE)
                            );
                            sentMessage.setLocalId(localId);
                            simpMessagingTemplate.convertAndSendToUser(
                                    user.getId().toString(), "/queue/messages",
                                    createResponseNetworkMessage(NetworkStatus.SUCCESS, sentMessage)
                            );
                        },
                        () -> simpMessagingTemplate.convertAndSendToUser(
                                userDetailsService.getUserid().toString(), "/queue/response",
                                    createNetworkMessage(NetworkStatus.ERROR)
                                ),
                        () -> simpMessagingTemplate.convertAndSendToUser(
                                userDetailsService.getUserid().toString(), "/queue/response",
                                createNetworkMessage(NetworkStatus.ERROR)
                        )
                );
                break;
        }
    }

    private void sendMessage(MessageDto messageDto, VoidParamsActionFunctional<Message, Conversation> ok, VoidActionFunctional emptyMessageDto, VoidActionFunctional noConversation) {
        if(isEmpty(messageDto)) {
            emptyMessageDto.action();
            return;
        }
        Conversation conversation = messageService.haveConversation(
                messageDto.getConversationId()
        );
        if(!isEmpty(conversation)) {
            Message message = new Message(messageDto);
            message.setConversationId(conversation.getId());
            message.setUserId(messageDto.getUserId());
            messageService.save(message);
            message.setId(message.getId());
            ok.action(message, conversation);
        }else{
            noConversation.action();
        }
    }

    private NetworkMessageDto createNetworkMessage(NetworkStatus networkStatus) {
        NetworkMessageDto responseMessage = new NetworkMessageDto();
        responseMessage.setNetworkAction(NetworkAction.RESPONSE);
        responseMessage.setNetworkOrigin(NetworkOrigin.SERVER);
        responseMessage.setNetworkStatus(networkStatus);
        return responseMessage;
    }

    private NetworkMessageDto createNetworkMessage(NetworkStatus networkStatus, NetworkAction action) {
        NetworkMessageDto responseMessage = new NetworkMessageDto();
        responseMessage.setNetworkAction(action);
        responseMessage.setNetworkOrigin(NetworkOrigin.SERVER);
        responseMessage.setNetworkStatus(networkStatus);
        return responseMessage;
    }

    private NetworkMessageDto createResponseNetworkMessage(NetworkStatus networkStatus, MessageDto data) {
        NetworkMessageDto responseMessage = new NetworkMessageDto();
        responseMessage.setNetworkAction(NetworkAction.RESPONSE_SEND);
        responseMessage.setNetworkOrigin(NetworkOrigin.SERVER);
        responseMessage.setNetworkStatus(networkStatus);
        responseMessage.setData(data);
        return responseMessage;
    }

    private NetworkMessageDto createNetworkMessage(NetworkStatus networkStatus, MessageDto data) {
        NetworkMessageDto responseMessage = createNetworkMessage(networkStatus);
        responseMessage.setData(data);
        return  responseMessage;
    }

}
