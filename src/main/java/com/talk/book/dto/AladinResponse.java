package com.talk.book.dto;

import com.talk.book.domain.BookType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AladinResponse {
    private BookType category;
    private List<BookResponse> item;
}
