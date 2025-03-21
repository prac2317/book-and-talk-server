package com.talk.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubResponseDTO {
    private int totalCount;
    private List<ClubListItemDTO> data;
}
