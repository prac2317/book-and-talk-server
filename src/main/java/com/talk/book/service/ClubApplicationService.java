package com.talk.book.service;

import com.talk.book.domain.Club;
import com.talk.book.domain.ClubApplication;
import com.talk.book.domain.Member;
import com.talk.book.dto.ApplicationRequestDTO;
import com.talk.book.repository.ClubApplicationRepository;
import com.talk.book.repository.ClubRepository;
import com.talk.book.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClubApplicationService {

    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ClubApplicationRepository clubApplicationRepository;

    public void applyToClub(Long clubId, Long hostId, ApplicationRequestDTO request) {

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("클럽을 찾을 수 없습니다."));

        Member member = memberRepository.findById(hostId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        // Todo: answer 길이 체크하기. 0이거나 200자 넘으면 안됨.
        if (request.getQuestionAnswer().isEmpty() || request.getQuestionAnswer().length() < 200) {
            throw new RuntimeException("답변은 200자 이내로 작성해주세요.");
        }

        // Todo: 중복 체크하기

        ClubApplication clubApplication = ClubApplication.builder()
                .member(member)
                .club(club)
                .isCompleted(false)
                .QuestionAnswer(request.getQuestionAnswer())
                .createdAt(LocalDateTime.now())
                .build();

        clubApplicationRepository.save(clubApplication);
    }
}
