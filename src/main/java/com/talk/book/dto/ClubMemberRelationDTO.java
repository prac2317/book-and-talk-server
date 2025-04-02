package com.talk.book.dto;

import com.talk.book.enumerate.ClubMemberRelationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubMemberRelationDTO {
    private ClubMemberRelationType relation;
}
