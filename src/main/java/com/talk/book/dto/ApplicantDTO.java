package com.talk.book.dto;

import com.talk.book.enumerate.ClubApplicationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDTO {

    private Long clubApplicationId;
    private String questionAnswer;
    private LocalDateTime createdAt;
    private ClubApplicationType status;

    private Long memberId;
    private String profileImage;
    private String nickname;

}
