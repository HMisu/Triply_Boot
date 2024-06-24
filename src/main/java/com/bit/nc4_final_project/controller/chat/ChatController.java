package com.bit.nc4_final_project.controller.chat;

import com.bit.nc4_final_project.dto.ResponseDTO;
import com.bit.nc4_final_project.dto.chat.ChatDTO;
import com.bit.nc4_final_project.dto.chat.ChatMakeInfo;
import com.bit.nc4_final_project.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/getChatList")
    public ResponseEntity<?> getChatList(@RequestParam("userId") String userId) {
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
        try {
            Map<String, Object> responseMap = new HashMap<>();
            List<ChatDTO> chatDTOList = chatService.getChatList(userId);

            responseMap.put("chatList", chatDTOList);
            responseDTO.setItem(responseMap);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorCode(100);
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(400);
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/make-chat")
    public ResponseEntity<?> makeChat(@RequestBody ChatMakeInfo chatMakeInfo) {
        ResponseDTO<List<ChatDTO>> responseDTO = new ResponseDTO<>();
        try {
            List<ChatDTO> returnChatDTOList = chatService.makeChatRoom(chatMakeInfo);
            responseDTO.setItem(returnChatDTOList);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorCode(100);
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(400);
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
