package com.talk.book.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubDTO {
    private Long clubId;
    private String name;
    private String bookTitle;
    private int maxParticipants;
    private int currentParticipant;
    private LocalDateTime startDate;
    private int duration;
    private String status;
    private String clubDescription;
    private String isbn13;
    private LocalDateTime createdAt;
    private String address;
    private Double longitude;
    private Double latitude;
    private String clubImage;
}

