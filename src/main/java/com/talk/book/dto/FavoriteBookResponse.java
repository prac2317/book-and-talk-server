package com.talk.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class FavoriteBookResponse {
    private int totalCount;
    private List<String> data; // 변경: Long → String
}
