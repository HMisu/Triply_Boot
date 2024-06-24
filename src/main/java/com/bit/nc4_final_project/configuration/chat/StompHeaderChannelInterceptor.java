package com.bit.nc4_final_project.configuration.chat;

import com.bit.nc4_final_project.jwt.JwtTokenProvider;
import com.bit.nc4_final_project.service.chat.ChatRoomSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHeaderChannelInterceptor implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomSessionService chatRoomSessionService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        if (command == StompCommand.CONNECT) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token == null || !token.startsWith("Bearer")) {
                throw new RuntimeException("Token is not valid.");
            } else if (jwtTokenProvider.validateAndGetUsername(token.substring(7)) == null) {
                throw new RuntimeException("The Token held by current user are unavailable.");
            }
        }

        if (command == StompCommand.SUBSCRIBE) {
            log.info("===== WebSocket InterCepTer : SUBSCRIBE =====");
            chatRoomSessionService.addSession(accessor.getDestination().substring(5), accessor.getSessionId());
        }

        if (command == StompCommand.SEND) {

        }

        if (command == StompCommand.DISCONNECT) {
            log.info("===== WebSocket InterCepTer : DISCONNECT =====");
            chatRoomSessionService.removeSession(accessor.getSessionId());
        }

        return message;
    }
}
