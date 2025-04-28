package com.talk.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
public class GetChatRoomParticipantsResponse {
    List<ChatRoomParticipantDTO> data;
}
