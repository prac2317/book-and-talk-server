package com.talk.book.dto;

import com.talk.book.domain.BookCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetBookListResponse {

    private BookCategory category;
    private List<BookSummary> data;
}
