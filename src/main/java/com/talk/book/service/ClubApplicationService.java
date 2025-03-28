package com.talk.book.service;

import com.talk.book.domain.Club;
import com.talk.book.domain.ClubApplication;
import com.talk.book.domain.Member;
import com.talk.book.dto.ApplicantListDTO;
import com.talk.book.dto.ApplicationRequestDTO;
import com.talk.book.dto.ApplicantDTO;
import com.talk.book.enumerate.ClubApplicationType;
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
            if (clubApplication.getStatus().equals(ClubApplicationType.PENDING)) {
                throw new RuntimeException("이미 가입 신청한 상태입니다.");
            }
        });

        ClubApplication clubApplication = ClubApplication.builder()
                .member(member)
                .club(club)
                .status(ClubApplicationType.PENDING)
                .QuestionAnswer(request.getQuestionAnswer())
                .createdAt(LocalDateTime.now())
                .build();

        clubApplicationRepository.save(clubApplication);
    }

    public ApplicantListDTO getApplicants(Long clubId, Long memberId){
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("클럽을 찾을 수 없습니다."));

        if (!club.getHost().getId().equals(memberId)) {
            throw new RuntimeException("호스트 외에 접근할 수 없습니다.");
        }

        List<ClubApplication> clubApplications = clubApplicationRepository.findByClubId(clubId);
        List<ApplicantDTO> applicantsDTOs = clubApplications.stream()
                .filter(clubApplication -> clubApplication.getStatus().equals(ClubApplicationType.PENDING))
                .map(clubApplication -> {
                    Member member = memberRepository.findById(clubApplication.getMember().getId())
                            .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));
                    ApplicantDTO getApplicantsDTO = new ApplicantDTO();
                    getApplicantsDTO.setClubApplicationId(clubApplication.getId());
                    getApplicantsDTO.setQuestionAnswer(clubApplication.getQuestionAnswer());
                    getApplicantsDTO.setCreatedAt(clubApplication.getCreatedAt());
                    getApplicantsDTO.setStatus(clubApplication.getStatus());
                    getApplicantsDTO.setMemberId(member.getId());
                    getApplicantsDTO.setProfileImage(member.getProfileImageUrl());
                    getApplicantsDTO.setNickname(member.getNickname());
                    return getApplicantsDTO;
                }).toList();

        return new ApplicantListDTO(applicantsDTOs);
    }
}
