//package com.talk.book.controller;
//
//import com.talk.book.dto.FavoriteClubRequest;
//import com.talk.book.repository.FavoriteBookRepository;
//import com.talk.book.security.ApiResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/favorites/books")
//@RequiredArgsConstructor
//public class FavoriteBookController {
//
//    private final FavoriteBookRepository favoriteBookRepository;
//
//    @PostMapping
//    public ResponseEntity<ApiResponse> addFavoriteBook(
//            HttpServletRequest request,
//            @RequestBody FavoriteClubRequest favoriteClubRequest) {
//        Long memberId = getHostIdFromCookie(request);
//        favoriteBookservice.addFavoriteBook(memberId, favoriteClubRequest.getIsbn13());
//        return ResponseEntity.ok(new ApiResponse("북이 즐겨찾기에 추가되었습니다."));
//    }
//
//    @DeleteMapping
//
//}
