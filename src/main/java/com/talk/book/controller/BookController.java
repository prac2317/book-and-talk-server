package com.talk.book.controller;

import com.talk.book.domain.BookType;
import com.talk.book.dto.AladinResponse;
import com.talk.book.dto.GetBookListResponse;
import com.talk.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<GetBookListResponse> getBookList() {
        GetBookListResponse response = bookService.getBookList();

        return ResponseEntity.ok(response);
    }
}
