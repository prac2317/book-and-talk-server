package com.talk.book.controller;


import com.talk.book.dto.ClubListItemDTO;
import com.talk.book.dto.ClubResponseDTO;
import com.talk.book.dto.FavoriteClubRequest;
import com.talk.book.dto.FavoriteClubResponse;
import com.talk.book.security.ApiResponse;
import com.talk.book.service.FavoriteClubService;
import com.talk.book.service.MemberService;
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
        Long memberId = memberService.getHostIdFromCookie(request);  // Extract memberId from cookie
        favoriteClubService.addFavoriteClub(memberId, favoriteClubRequest.getClubId());
        return ResponseEntity.ok(new ApiResponse("클럽이 즐겨찾기에 추가되었습니다."));
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<ApiResponse> removeFavoriteClub(
            @PathVariable Long clubId,
            HttpServletRequest request) {
        Long memberId = memberService.getHostIdFromCookie(request);  // Extract memberId from cookie
        favoriteClubService.removeFavoriteClub(memberId, clubId);
        return ResponseEntity.ok(new ApiResponse("클럽이 즐겨찾기에서 삭제되었습니다."));
    }

    @GetMapping
    public ResponseEntity<ClubResponseDTO> getFavoriteClubs(HttpServletRequest request) {
        Long memberId = memberService.getHostIdFromCookie(request);  // Extract memberId from cookie
        List<FavoriteClubResponse> favorites = favoriteClubService.getFavoriteClubs(memberId);
        List<ClubListItemDTO> clubListItems = favorites.stream()
                .map(FavoriteClubResponse::toClubListItemDTO)
                .collect(Collectors.toList());
        ClubResponseDTO response = new ClubResponseDTO(clubListItems.size(), clubListItems);
        return ResponseEntity.ok(response);
    }
}
