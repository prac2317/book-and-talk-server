package com.talk.book.dto;

import com.talk.book.domain.ChatRoom;
import com.talk.book.domain.Member;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class ChatDTO {

    private Long id;

    private Long senderId;

    private String content;

    private Long ChatRoomId;

    private LocalDateTime createdAt;
}
