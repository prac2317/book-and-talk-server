package com.talk.book.service;

import com.talk.book.domain.Member;
import com.talk.book.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final HttpServletResponse response;
    private final PasswordEncoder passwordEncoder;

    public Member signup(String email, String nickname, String password) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
        Member member = new Member();
        member.setEmail(email);
        member.setNickname(nickname);
        member.setPassword(passwordEncoder.encode(password));
        member.setPassword(password);
        return memberRepository.save(member);
    }

    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        ResponseCookie cookie = ResponseCookie.from("memberId", String.valueOf(member.getId()))
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(3600)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return UUID.randomUUID().toString();
    }


    public Long getMemberIdFromCookie(HttpServletRequest request) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //Todo: 예외 수정하기
        if (user != null) {
            Member member = memberRepository.findByEmail(user.getUsername()).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
            return member.getId();
        } else {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
