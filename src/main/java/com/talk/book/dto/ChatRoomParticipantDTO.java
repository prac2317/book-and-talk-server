package com.talk.book.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
public class ChatRoomParticipantDTO {
    private Long memberId;
    private String nickname;
    private String imageUrl;
    private Boolean isRoomHost;
}
