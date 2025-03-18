package com.talk.book.controller;

import com.talk.book.domain.Club;
import com.talk.book.dto.ClubRequest;
import com.talk.book.service.ClubService;
import com.talk.book.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/clubs")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;
    private final MemberService memberService;

    @GetMapping("/search")
    public ResponseEntity<List<Club>> getClubsByIsbn(@RequestParam String isbn13) {
        List<Club> clubs = clubService.getClubsByIsbn(isbn13);
        return ResponseEntity.ok(clubs);
    }

    @PostMapping
    public ResponseEntity<Club> createClub(
            @RequestBody ClubRequest request,
            HttpServletRequest httpRequest) {

        Long hostId = memberService.getHostIdFromCookie(httpRequest);
        Club club = clubService.createClub(request, hostId);
        return ResponseEntity.ok(club);
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<Club> getClubById(@PathVariable Long clubId) {
        Optional<Club> club = clubService.getClubById(clubId);
        return club.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<String> deleteClub(@PathVariable Long clubId) {
        clubService.deleteClub(clubId);
        return ResponseEntity.ok("클럽이 삭제되었습니다.");
    }
}

