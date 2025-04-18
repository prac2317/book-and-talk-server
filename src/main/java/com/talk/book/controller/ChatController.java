package com.talk.book.controller;

import com.talk.book.domain.Chat;
import com.talk.book.dto.ChatDTO;
import com.talk.book.dto.GetChatRoomsResponse;
import com.talk.book.service.ChatService;
import com.talk.book.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;
    private final MemberService memberService;

    @MessageMapping("/chat")
    public void chat(ChatDTO chatDTO) {
        ChatDTO newChatDTO = chatService.saveChat(chatDTO);
        template.convertAndSend("/topic/chat", newChatDTO);
    }

    @GetMapping("/api/v1/chat/chatrooms")
    public ResponseEntity<GetChatRoomsResponse> getChatRooms(HttpServletRequest httpRequest) {
        Long memberId = memberService.getHostIdFromCookie(httpRequest);
        GetChatRoomsResponse response = chatService.getChatRooms(memberId);
        return ResponseEntity.ok(response);
    }
}
