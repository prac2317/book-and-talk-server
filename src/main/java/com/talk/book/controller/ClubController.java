package com.talk.book.controller;

import com.talk.book.domain.Club;
import com.talk.book.domain.Member;
import com.talk.book.dto.*;

import com.talk.book.service.ClubService;
import com.talk.book.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/clubs")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<ClubResponseDTO> getClubsByIsbn(@RequestParam String isbn13) {
        ClubResponseDTO response = clubService.getClubsByIsbn(isbn13);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> createClub(
            @RequestBody ClubRequest request,
            HttpServletRequest httpRequest) {

        Long hostId = memberService.getHostIdFromCookie(httpRequest);
        if (hostId == null) {
            throw new IllegalArgumentException("호스트 ID를 찾을 수 없습니다.");
        }

        clubService.createClub(request, hostId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<ClubDTO> getClubById(@PathVariable Long clubId) {
        ClubDTO club = clubService.getClubDetailById(clubId);
        return ResponseEntity.ok(club);
    }

    @GetMapping("/{clubId}/member")
    public ResponseEntity<ParticipantListDTO> getClubParticipants(@PathVariable Long clubId) {
        ParticipantListDTO participants = clubService.getClubParticipants(clubId);
        return ResponseEntity.ok(participants);
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long clubId) {
        clubService.deleteClub(clubId);
        return ResponseEntity.ok().build();
    }
}


