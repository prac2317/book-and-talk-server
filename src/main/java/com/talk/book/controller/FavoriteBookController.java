package com.talk.book.controller;

import com.talk.book.dto.BookFavoriteRelationResponse;
import com.talk.book.dto.FavoriteBookRequest;
import com.talk.book.dto.FavoriteBookResponse;
import com.talk.book.dto.FavoriteClubRequest;
import com.talk.book.repository.FavoriteBookRepository;
import com.talk.book.security.ApiResponse;
import com.talk.book.service.FavoriteBookService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/favorites/books")
@RequiredArgsConstructor
public class FavoriteBookController {

    private final FavoriteBookService favoriteBookService;

    @PostMapping
    public ResponseEntity<ApiResponse> addFavoriteBook(
            HttpServletRequest request,
            @RequestBody FavoriteBookRequest favoriteBookRequest) {
        Long memberId = getMemberIdFromCookie(request);
        favoriteBookService.addFavoriteBook(memberId, favoriteBookRequest.getIsbn13());
        return ResponseEntity.ok(new ApiResponse("책이 즐겨찾기에 추가되었습니다."));
    }

    @DeleteMapping("/{isbn13}")
    public ResponseEntity<ApiResponse> removeFavoriteBook(
            HttpServletRequest request,
            @PathVariable String isbn13) {
        Long memberId = getMemberIdFromCookie(request);
        favoriteBookService.removeFavoriteBook(memberId, isbn13);
        return ResponseEntity.ok(new ApiResponse("책이 즐겨찾기에서 삭제되었습니다."));
    }

    @GetMapping
    public ResponseEntity<FavoriteBookResponse> getFavoriteBooks(HttpServletRequest request) {
        Long memberId = getMemberIdFromCookie(request);
        List<String> favorites = favoriteBookService.getFavoriteBooks(memberId);
        FavoriteBookResponse response = new FavoriteBookResponse(favorites.size(), favorites);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/relation")
    public ResponseEntity<BookFavoriteRelationResponse> isFavoriteBook(
            HttpServletRequest request,
            @RequestParam String isbn13) {

        log.info("isFavoriteBook isbn13 : {}", isbn13);
        Long memberId = getMemberIdFromCookie(request);
        boolean exists = favoriteBookService.isFavoriteBook(memberId, isbn13);

        BookFavoriteRelationResponse response = new BookFavoriteRelationResponse();
        response.setIsFavorite(exists);
        return ResponseEntity.ok(response);
    }


    public Long getMemberIdFromCookie(HttpServletRequest request) {
        log.info("getHostIdFromCookie");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("memberId".equals(cookie.getName())) {
                    try {
                        return Long.parseLong(cookie.getValue());
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("유효하지 않은 memberId 쿠키 값입니다.");
                    }
                }
            }
        }
        throw new IllegalArgumentException("memberId 쿠키가 존재하지 않습니다.");
    }
}
