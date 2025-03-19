package com.talk.book.controller;

import com.talk.book.domain.BookCategory;
import com.talk.book.dto.GetBookListResponse;
import com.talk.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<GetBookListResponse> getBookList(@RequestParam String category) {
        GetBookListResponse response = bookService.getBookList(category);

        return ResponseEntity.ok(response);
    }
}
