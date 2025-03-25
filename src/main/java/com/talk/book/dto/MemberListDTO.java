package com.talk.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemberListDTO {
    private List<MemberDTO> members;
}
