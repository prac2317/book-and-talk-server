package com.talk.book.controller;

import com.talk.book.domain.Club;
import com.talk.book.domain.Member;
import com.talk.book.dto.*;

import com.talk.book.service.ClubService;
import com.talk.book.service.MemberService;
import com.talk.book.service.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/clubs")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;
    private final MemberService memberService;
    private final S3Service s3Service;

    @GetMapping
    public ResponseEntity<ClubResponseDTO> getClubsByIsbn(@RequestParam String isbn13) {
        ClubResponseDTO response = clubService.getClubsByIsbn(isbn13);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nearby")
    public ResponseEntity<ClubListNearbyResponseDTO> getNearbyClubs(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ClubListNearbyResponseDTO response = clubService.getNearbyClubs(longitude, latitude, page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createClub(
            @RequestPart("request") ClubRequest request,
            @RequestPart("image") MultipartFile image,
            HttpServletRequest httpRequest) throws IOException {

        Long memberId = memberService.getMemberIdFromCookie(httpRequest);
        if (memberId == null) {
            throw new IllegalArgumentException("사용자의 ID를 찾을 수 없습니다.");
        }

        String imageUrl = s3Service.uploadFile(image, "clubs");
        request.setClubImage(imageUrl);

        clubService.createClub(request, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<ClubDTO> getClubById(@PathVariable Long clubId) {
        ClubDTO club = clubService.getClubDetailById(clubId);
        return ResponseEntity.ok(club);
    }

    @GetMapping("/{clubId}/member")
    public ResponseEntity<MemberListDTO> getClubMembers(@PathVariable Long clubId) {
        MemberListDTO members = clubService.getClubMembers(clubId);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{clubId}/relation")
    public ResponseEntity<?> getRelation (
            @PathVariable Long clubId,
            HttpServletRequest httpRequest
    ) {
        Long memberId = memberService.getMemberIdFromCookie(httpRequest);
        ClubMemberRelationDTO relation = clubService.getRelation(clubId, memberId);
        return ResponseEntity.ok(relation);
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long clubId) {
        clubService.deleteClub(clubId);
        return ResponseEntity.ok().build();
    }
}


