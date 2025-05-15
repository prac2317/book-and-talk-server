package com.talk.book.controller;


import com.talk.book.dto.*;
import com.talk.book.security.ApiResponse;
import com.talk.book.service.FavoriteClubService;
import com.talk.book.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/favorites/clubs")
@RequiredArgsConstructor
public class FavoriteClubController {

    private final FavoriteClubService favoriteClubService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse> addFavoriteClub(
            HttpServletRequest request,
            @RequestBody FavoriteClubRequest favoriteClubRequest) {
        Long memberId = memberService.getMemberIdFromCookie(request);  // Extract memberId from cookie
        favoriteClubService.addFavoriteClub(memberId, favoriteClubRequest.getClubId());
        return ResponseEntity.ok(new ApiResponse("클럽이 즐겨찾기에 추가되었습니다."));
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<ApiResponse> removeFavoriteClub(
            @PathVariable Long clubId,
            HttpServletRequest request) {
        Long memberId = memberService.getMemberIdFromCookie(request);  // Extract memberId from cookie
        favoriteClubService.removeFavoriteClub(memberId, clubId);
        return ResponseEntity.ok(new ApiResponse("클럽이 즐겨찾기에서 삭제되었습니다."));
    }

    @GetMapping
    public ResponseEntity<ClubResponseDTO> getFavoriteClubs(HttpServletRequest request) {
        Long memberId = memberService.getMemberIdFromCookie(request);  // Extract memberId from cookie
        List<FavoriteClubResponse> favorites = favoriteClubService.getFavoriteClubs(memberId);
        List<ClubListItemDTO> clubListItems = favorites.stream()
                .map(FavoriteClubResponse::toClubListItemDTO)
                .collect(Collectors.toList());
        ClubResponseDTO response = new ClubResponseDTO(clubListItems.size(), clubListItems);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/relation")
    public ResponseEntity<ClubFavoriteRelationResponse> isFavoriteClub(
            HttpServletRequest request,
            @RequestParam Long clubId) {
        Long memberId = getMemberIdFromCookie(request);
        boolean exists = favoriteClubService.isFavoriteClub(memberId, clubId);

        ClubFavoriteRelationResponse response = new ClubFavoriteRelationResponse();
        response.setIsFavorite(exists);
        return ResponseEntity.ok(response);
    }

    public Long getMemberIdFromCookie(HttpServletRequest request) {
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