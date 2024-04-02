package com.bit.nc4_final_project.repository.chatroom;

import com.bit.nc4_final_project.repository.chat.ChatRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.bit.nc4_final_project.entity.chat.ChatMessage;

import java.util.List;

public interface ChatRoomRepository extends MongoRepository <ChatMessage, String> {
    List<ChatMessage> findAllByChatRoomId(String chatRoomId);
}
