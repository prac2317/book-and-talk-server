package com.talk.book.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoriteBookRequest {
    private String isbn13;
}