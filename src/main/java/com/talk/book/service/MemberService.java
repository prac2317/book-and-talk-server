package com.talk.book.service;

import com.talk.book.domain.Member;
import com.talk.book.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
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

        return UUID.randomUUID().toString();
    }
}
