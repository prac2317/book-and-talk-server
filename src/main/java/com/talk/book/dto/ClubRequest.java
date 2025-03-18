package com.talk.book.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClubRequest {
    private String isbn13;
    private String bookTitle;
    private String name;
    private String location;
    private int maxParticipants;
    private String status;
    private LocalDateTime startDate;
    private int duration;
    private String clubDescription;
    private String clubImage;
}

