package com.talk.book.service;

import com.talk.book.domain.Member;
import com.talk.book.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final HttpServletResponse response;
//    private final PasswordEncoder passwordEncoder;

    public Member signup(String email, String nickname, String password) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
        Member member = new Member();
        member.setEmail(email);
        member.setNickname(nickname);
//        member.setPassword(passwordEncoder.encode(password));
        member.setPassword(password);
        return memberRepository.save(member);
    }

    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));

//        if (!passwordEncoder.matches(password, member.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }

        Cookie cookie = new Cookie("hostId", String.valueOf(member.getId()));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return UUID.randomUUID().toString();
    }

    public Long getHostIdFromCookie(HttpServletRequest request) {
        System.out.println("getHostIdFromCookie");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("hostId".equals(cookie.getName())) { // "hostId" 쿠키 확인
                    try {
                        return Long.parseLong(cookie.getValue());
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("유효하지 않은 hostId 쿠키 값입니다.");
                    }
                }
            }
        }
        throw new IllegalArgumentException("hostId 쿠키가 존재하지 않습니다.");
    }
}
