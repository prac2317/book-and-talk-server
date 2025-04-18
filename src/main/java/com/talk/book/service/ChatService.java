package com.talk.book.service;

import com.talk.book.domain.*;
import com.talk.book.dto.*;
import com.talk.book.repository.*;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;

    @Transactional
    public ChatDTO saveChat(ChatDTO chatDTO) {
        Member member = memberRepository.findById(chatDTO.getSenderId()).orElseThrow(() -> new RuntimeException("사용자가 없습니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(chatDTO.getChatRoomId()).orElseThrow(() -> new RuntimeException("채팅방이 없습니다."));

        Chat chat = new Chat();
        chat.setSender(member);
        chat.setRoom(chatRoom);
        chat.setContent(chatDTO.getContent());
        chat.setCreatedAt(LocalDateTime.now());
        Chat savedChat = chatRepository.save(chat);

        // 나중에 setter 빼고 나머지 2개 업데이트하는 메서드 만들기
        chatRoom.setRecentChat(chat);
        chatRoom.setUpdatedAt(savedChat.getCreatedAt());

        ChatDTO chatResponseDTO = ChatDTO.builder()
                        .id(savedChat.getId())
                        .senderId(savedChat.getSender().getId())
                        .chatRoomId(savedChat.getRoom().getId())
                        .createdAt(savedChat.getCreatedAt())
                        .build();
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

    public GetChatRoomParticipantsResponse getParticipants (Long chatRoomId) {
        List<MemberChatRoom> memberChatRooms = memberChatRoomRepository.findByChatRoomId(chatRoomId);

        List<ChatRoomParticipantDTO> participantDTOs = new ArrayList<>();
        for (MemberChatRoom memberChatRoom : memberChatRooms) {
            Member member = memberChatRoom.getMember();
            ChatRoomParticipantDTO participantDTO = ChatRoomParticipantDTO.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .imageUrl(member.getProfileImageUrl())
                    .isRoomHost(memberChatRoom.getIsHost())  // 질문하기: 이름 변경
                    .build();
            participantDTOs.add(participantDTO);
        }

        return new GetChatRoomParticipantsResponse(participantDTOs);
    }

    public GetChatRoomMessagesResponse getMessages(Long chatRoomId) {
        List<Chat> chats = chatRepository.findByRoomId(chatRoomId);

        List<ChatDTO> chatDTOs = chats.stream()
                .map(chat -> ChatDTO.builder()
                        .id(chat.getId())
                        .senderId(chat.getSender().getId())
                        .chatRoomId(chatRoomId)
                        .content(chat.getContent())
                        .createdAt(chat.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return new GetChatRoomMessagesResponse(chatDTOs);
    }
}