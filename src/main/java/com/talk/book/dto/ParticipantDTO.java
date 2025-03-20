package com.talk.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParticipantDTO {
    private boolean isHost;
    private Long memberId;
    private String nickname;
}
