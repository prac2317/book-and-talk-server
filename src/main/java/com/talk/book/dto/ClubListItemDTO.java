package com.talk.book.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubListItemDTO {
    private Long clubId;
    private String bookTitle;
    private String name;
    private int currentParticipants;
    private int maxParticipants;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;
}
