package com.talk.book.config;


import com.talk.book.domain.Club;
import com.talk.book.domain.Member;
import com.talk.book.repository.ClubRepository;
import com.talk.book.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
//    private final ClubRepository clubRepository;

    @Override
    public void run(String... args) throws Exception {

        for (int i = 0; i < 5; i++) {
            Member member = new Member();
            member.setEmail("email" + Integer.toString(i) + "@gmail.com");
            member.setNickname("nickname" + Integer.toString(i));
            member.setPassword("password" + Integer.toString(i));
            Member savedMember = memberRepository.save(member);

//            Club club = new Club();
//            club.setHost(savedMember);
//            club.setIsbn13("9791162540640");
//            club.setName("독서모임" + Integer.toString(i));
//            club.setBookTitle("아주 작은 습관의 힘");
//            club.setMaxParticipants(5 + i);
//            club.setCurrentParticipants(1);
//            club.setStatus(ClubStatus.RECRUITING);
//            club.setStartDate(LocalDateTime.now());
//            club.setDuration(3 + i);
//            club.setClubDescription("즐거운 만남 가져요!" + Integer.toString(i));
//            club.setCreatedAt(LocalDateTime.now());
//            clubRepository.save(club);
        }
    }
}

