package com.talk.book.dto;

import com.talk.book.domain.ChatRoom;
import com.talk.book.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
public class ChatDTO {

    private Long id;
    private Long senderId;
    private String content;
    private Long chatRoomId;
    private LocalDateTime createdAt;
}
