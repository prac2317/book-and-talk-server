package com.talk.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookResponse {

    private String title;
    private String isbn13;
    private String cover;
}
