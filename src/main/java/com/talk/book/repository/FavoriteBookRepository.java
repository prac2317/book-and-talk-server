package com.talk.book.repository;

import com.talk.book.domain.Book;
import com.talk.book.domain.FavoriteBook;
import com.talk.book.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long> {
    List<FavoriteBook> findByMemberId(Long memberId);
    void deleteByMemberIdAndIsbn13(Long memberId, Long isbn13);
}