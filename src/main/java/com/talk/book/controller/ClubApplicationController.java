package com.talk.book.controller;

import com.talk.book.dto.ApplicantListDTO;
import com.talk.book.dto.ApplicationProcessRequestDTO;
import com.talk.book.dto.ApplicationRequestDTO;
import com.talk.book.dto.ApplicantDTO;
import com.talk.book.service.ClubApplicationService;
import com.talk.book.service.ClubService;
import com.talk.book.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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
        Long hostId = memberService.getMemberIdFromCookie(httpRequest);
        clubApplicationService.applyToClub(clubId, hostId, request);

        return ResponseEntity.ok("참가 신청 완료");
    }

    @GetMapping("/api/v1/clubs/{clubId}/applications")
    public ResponseEntity<ApplicantListDTO> getApplicants(
            @PathVariable Long clubId,
            HttpServletRequest httpRequest
    ) {
        Long memberId = memberService.getMemberIdFromCookie(httpRequest);
        ApplicantListDTO response = clubApplicationService.getApplicants(clubId, memberId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/v1/clubs/{clubId}/applications/process")
    public ResponseEntity<?> processApplication(
            @PathVariable Long clubId,
            @RequestBody ApplicationProcessRequestDTO request,
            HttpServletRequest httpRequest
    ) {
        //Todo: host인지 검증하는 예외 추가할 것

        clubApplicationService.processApplication(request.getClubApplicationId(), request.getMemberId(), clubId, request.getProcessType());
        return ResponseEntity.ok("참가 승인/거절 완료");
    }

    @PostMapping("/api/v1/clubs/{clubId}/applications/cancel")
    public ResponseEntity<?> cancelApplication(
            @PathVariable Long clubId,
            HttpServletRequest httpRequest
    ) {
        System.out.println("clubApplicationController - cancelApplication 진입");
        Cookie[] cookies = httpRequest.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println("쿠키 출력");
            System.out.println(cookie.getName());
            System.out.println(cookie.getValue());
        }
        Long memberId = memberService.getMemberIdFromCookie(httpRequest);
        clubApplicationService.cancelApplication(clubId, memberId);

        return ResponseEntity.ok("참가 신청 취소 완료");
    }

}
