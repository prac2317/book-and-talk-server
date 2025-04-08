package com.talk.book.repository;

import com.talk.book.domain.FavoriteBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long> {
    List<FavoriteBook> findByMemberId(Long memberId);

    void deleteByMemberIdAndIsbn13(Long memberId, String isbn13);

    boolean existsByMemberIdAndIsbn13(Long memberId, String isbn13);
}
