package com.talk.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberDTO {
    private boolean isHost;
    private Long memberId;
    private String nickname;
}
