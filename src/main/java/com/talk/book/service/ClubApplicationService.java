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
import java.util.List;

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

        if (request.getQuestionAnswer().isEmpty() || request.getQuestionAnswer().length() > 200) {
            throw new RuntimeException("답변은 200자 이내로 작성해주세요.");
        }

        List<ClubApplication> clubApplications = clubApplicationRepository.findByMemberIdAndClubId(hostId, clubId);
        clubApplications.forEach(clubApplication -> {
            if (!clubApplication.getIsCompleted()) {
                throw new RuntimeException("이미 가입 신청한 상태입니다.");
            }
        });

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
