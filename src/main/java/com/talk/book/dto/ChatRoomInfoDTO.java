package com.talk.book.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@Builder
public class ChatRoomInfoDTO {
    private Long chatRoomId;
    private String image;
    private String name;
    private LocalDateTime updatedAt;
    private List<Long> memberIdList;
    private String recentMessage;
}
