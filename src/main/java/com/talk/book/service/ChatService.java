package com.talk.book.service;

import com.talk.book.domain.Chat;
import com.talk.book.domain.ChatRoom;
import com.talk.book.domain.Member;
import com.talk.book.dto.ChatDTO;
import com.talk.book.repository.ChatRepository;
import com.talk.book.repository.ChatRoomRepository;
import com.talk.book.repository.ClubRepository;
import com.talk.book.repository.MemberRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public ChatDTO saveChat(ChatDTO chatDTO) {
        Member member = memberRepository.findById(chatDTO.getSenderId()).orElseThrow(() -> new RuntimeException("사용자가 없습니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(chatDTO.getChatRoomId()).orElseThrow(() -> new RuntimeException("채팅방이 없습니다."));

        Chat chat = new Chat();
        chat.setSender(member);
        chat.setRoom(chatRoom);
        chat.setContent(chatDTO.getContent());
        chat.setCreatedAt(LocalDateTime.now());
        Chat savedChat = chatRepository.save(chat);

        ChatDTO chatResponseDTO = new ChatDTO();
        chatResponseDTO.setId(savedChat.getId());
        chatResponseDTO.setSenderId(savedChat.getSender().getId());
        chatResponseDTO.setChatRoomId(savedChat.getRoom().getId());
        chatResponseDTO.setContent(savedChat.getContent());
        chatResponseDTO.setCreatedAt(savedChat.getCreatedAt());

        return chatResponseDTO;
    }
}