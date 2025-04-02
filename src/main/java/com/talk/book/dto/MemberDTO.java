package com.talk.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberDTO {
    private Boolean isHost;
    private Long memberId;
    private String nickname;
}
