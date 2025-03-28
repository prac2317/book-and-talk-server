package com.talk.book.controller;

import com.talk.book.dto.ApplicantListDTO;
import com.talk.book.dto.ApplicationRequestDTO;
import com.talk.book.dto.ApplicantDTO;
import com.talk.book.service.ClubApplicationService;
import com.talk.book.service.ClubService;
import com.talk.book.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ClubApplicationController {

    private final MemberService memberService;
    private final ClubService clubService;
    private final ClubApplicationService clubApplicationService;

    @PostMapping("/api/v1/clubs/{clubId}/applications")
    public ResponseEntity<?> applyToClub(
            @PathVariable Long clubId,
            @RequestBody ApplicationRequestDTO request,
            HttpServletRequest httpRequest
    ) {
        Long hostId = memberService.getHostIdFromCookie(httpRequest);
        clubApplicationService.applyToClub(clubId, hostId, request);

        return ResponseEntity.ok("참가 신청 완료");
    }

    @GetMapping("/api/v1/clubs/{clubId}/applications")
    public ResponseEntity<ApplicantListDTO> getApplicants(
            @PathVariable Long clubId,
            HttpServletRequest httpRequest
    ) {
        Long memberId = memberService.getHostIdFromCookie(httpRequest);
        ApplicantListDTO response = clubApplicationService.getApplicants(clubId, memberId);

        return ResponseEntity.ok(response);
    }

}
