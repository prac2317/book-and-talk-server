package com.talk.book.service;

import com.talk.book.domain.*;
import com.talk.book.dto.ChatDTO;
import com.talk.book.dto.ChatRoomInfoDTO;
import com.talk.book.dto.GetChatRoomsResponse;
import com.talk.book.repository.*;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;

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

    public GetChatRoomsResponse getChatRooms(Long memberId) {

        List<MemberChatRoom> memberChatRooms = memberChatRoomRepository.findByMemberId(memberId);

        // Todo: join으로 수정하기
        List<ChatRoomInfoDTO> chatRoomInfoDTOList = new ArrayList<>();
        for (MemberChatRoom memberChatRoom : memberChatRooms) {
            ChatRoom chatRoom = memberChatRoom.getChatRoom();
            List<Long> memberIdList = memberChatRoomRepository.findByChatRoomId(chatRoom.getId()).stream().map(relation -> relation.getMember().getId()).toList();
            Club club = chatRoom.getClub();
            ChatRoomInfoDTO chatRoomInfoDTO = ChatRoomInfoDTO.builder()
                    .chatRoomId(chatRoom.getId())
                    .name(chatRoom.getName())
                    .image(club.getClubImage())
                    .memberIdList(memberIdList)
                    .recentMessage(chatRoom.getRecentChat() == null ? "새 채팅방입니다." : chatRoom.getRecentChat().getContent())
                    .updatedAt(chatRoom.getUpdatedAt())
                    .build();
            chatRoomInfoDTOList.add(chatRoomInfoDTO);
        }

        return new GetChatRoomsResponse(chatRoomInfoDTOList);
    }
}