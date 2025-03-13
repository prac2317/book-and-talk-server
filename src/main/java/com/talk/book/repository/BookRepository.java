package com.talk.book.repository;

import com.talk.book.domain.Book;
import com.talk.book.domain.BookType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<List<Book>> findByType(BookType type);
}
