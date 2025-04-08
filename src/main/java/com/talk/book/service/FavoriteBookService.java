package com.talk.book.service;

import com.talk.book.domain.Book;
import com.talk.book.domain.FavoriteBook;
import com.talk.book.domain.Member;
import com.talk.book.repository.BookRepository;
import com.talk.book.repository.FavoriteBookRepository;
import com.talk.book.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteBookService {

    private final FavoriteBookRepository favoriteBookRepository;
    private final MemberRepository memberRepository;

    public boolean isFavoriteBook(Long memberId, String isbn13) {
        return favoriteBookRepository.existsByMemberIdAndIsbn13(memberId, isbn13);
    }

    @Transactional
    public void addFavoriteBook(Long memberId, String isbn13) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        FavoriteBook favoriteBook = FavoriteBook.builder()
                .member(member)
                .isbn13(isbn13)
                .build();
        favoriteBookRepository.save(favoriteBook);
    }

    @Transactional
    public void removeFavoriteBook(Long memberId, String isbn13) {
        favoriteBookRepository.deleteByMemberIdAndIsbn13(memberId, isbn13);
    }

    @Transactional
    public List<String> getFavoriteBooks(Long memberId) {
        return favoriteBookRepository.findByMemberId(memberId)
                .stream()
                .map(FavoriteBook::getIsbn13)
                .collect(Collectors.toList());
    }
}
