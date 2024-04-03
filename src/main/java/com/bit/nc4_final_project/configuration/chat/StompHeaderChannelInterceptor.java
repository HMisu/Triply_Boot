package com.bit.nc4_final_project.configuration.chat;

import com.bit.nc4_final_project.jwt.JwtTokenProvider;
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
@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 제일 먼저 실행하기 위해 우선순위를 높게 설정
public class StompHeaderChannelInterceptor implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
//        // websocket 연결시 헤더의 jwt token 유효성 검증
//        if (command == StompCommand.CONNECT) {
//            log.debug("===== WebSocket InterCepTer : CONNECT =====");
//            String token = accessor.getFirstNativeHeader("ACCESS_TOKEN");
//            // 현재 프로젝트 내의 jwtTokenProvider를 사용하여 토큰의 유효성을 검사
//            if (jwtTokenProvider.validateAndGetUsername(token) == null) {
//                log.info("===== WebSocket InterCepTer : Token is not valid. =====");
//                throw new AccessDeniedException("Token is not valid.");
//            }
//        }
//        String currentUserName = getCurrentUser().getUsername();
//        handleMessage(command, accessor, currentUserName);
        log.info("intercepted massage : {}", message);
        log.info("intercepted accessor : {}", accessor);
        log.info("intercepted command : {}", command);
        return message;
    }

    // 현재 세션에 담긴 사용자 정보를 가져오기 위한 메소드
//    public UserDetails getCurrentUser() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            return (UserDetails) principal;
//        }
//        return null;
//    }

    // stomp command에 따라 처리할 내용을 분기하는 메소드
//    private void handleMessage(StompCommand command, StompHeaderAccessor accessor, String currentUserName) {
//        switch (command) {
//            case CONNECT:
//
//                break;
//            case SUBSCRIBE:
//            case SEND:
//
//
//        }
//    }

//    private void connectToChatRoom(StompHeaderAccessor accessor, String currentUserName) {
//        // 채팅방 입장시 사용자 정보를 채팅방에 등록
//
//    }
}
