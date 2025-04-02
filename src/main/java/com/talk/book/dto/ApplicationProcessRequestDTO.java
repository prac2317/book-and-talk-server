package com.talk.book.dto;

import com.talk.book.enumerate.ProcessType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationProcessRequestDTO {
    private Long memberId;
    private Long clubApplicationId;
    private ProcessType processType;
}
