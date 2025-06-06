package com.talk.book.controller;

import com.talk.book.dto.LoginRequest;
import com.talk.book.dto.SignupRequest;
import com.talk.book.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        memberService.signup(request.getEmail(), request.getNickname(), request.getPassword());
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String sessionId = memberService.login(request.getEmail(), request.getPassword());

//        Cookie cookie = new Cookie("sessionId", sessionId);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        cookie.setMaxAge(3600);
//        response.addCookie(cookie);

        return  ResponseEntity.ok("로그인 성공");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
//        Cookie cookie = new Cookie("sessionId", null);
//        cookie.setMaxAge(0);
//        cookie.setPath("/");
//        response.addCookie(cookie);

        return ResponseEntity.ok("로그아웃 완료");
    }

    // TODO: 쿠키 -> 토큰으로 수정할 떄 지우기
    @GetMapping("/member")
    public ResponseEntity<Long> getMemberId(HttpServletRequest httpRequest) {
        System.out.println("getMemberId");
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("쿠키 이름: " + cookie.getName() + ", 값: " + cookie.getValue());
            }
        } else {
            System.out.println("쿠키가 없습니다!");
        }

        Long memberId = memberService.getHostIdFromCookie(httpRequest);

        return ResponseEntity.ok(memberId);
    }

}
