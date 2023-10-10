package com.example.sbwebsocketchatapp.config;

import com.example.sbwebsocketchatapp.chat.ChatMessage;
import com.example.sbwebsocketchatapp.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null) {
            log.info("User Disconnected: {}", username);
            var chatMessage = ChatMessage
                    .builder()
                    .type(MessageType.LEAVER)
                    .sender(username)
                    .build();
            messageTemplate.convertAndSend("/topic/public",chatMessage);
        }
    }
}
