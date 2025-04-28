package com.talk.book.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
public class GetChatRoomsResponse {
    List<ChatRoomInfoDTO> data;
}
