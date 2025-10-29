package com.talk.book.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubRequest {
    private String bookTitle;
    private String name;
    private int maxParticipants;
    private String clubDescription;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "UTC")
    private LocalDateTime startDate;

    private int duration;
    private String isbn13;

    private String address;
    private String latitude;
    private String longitude;

    private String clubImage;
}

