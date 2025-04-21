package com.talk.book.service;

import com.talk.book.domain.*;
import com.talk.book.dto.ApplicantListDTO;
import com.talk.book.dto.ApplicationRequestDTO;
import com.talk.book.dto.ApplicantDTO;
import com.talk.book.enumerate.ClubApplicationType;
import com.talk.book.enumerate.ProcessType;
import com.talk.book.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClubApplicationService {

    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final MemberClubRepository memberClubRepository;
    private final ClubApplicationRepository clubApplicationRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;

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

    public void processApplication(Long clubApplicationId, Long memberId, Long clubId, ProcessType processType) {
        ClubApplication clubApplication = clubApplicationRepository.findById(clubApplicationId)
                .orElseThrow(() -> new RuntimeException("참가 신청이 없습니다."));

        if (!clubApplication.getStatus().equals(ClubApplicationType.PENDING)) {
            throw new RuntimeException("대기 중인 참가 신청이 없습니다.");
        }

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("클럽을 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        if (processType.equals(ProcessType.APPROVE)) {
            MemberClub memberClub = new MemberClub();
            memberClub.setMember(member);
            memberClub.setClub(club);
            memberClub.setIsHost(false);
            memberClubRepository.save(memberClub);

            MemberChatRoom memberChatRoom = MemberChatRoom.builder()
                    .chatRoom(chatRoomRepository.findByClubId(clubId)) // 나중에 club에 ChatRoom 추가하기
                    .member(member)
                    .isHost(false)
                    .build();
            memberChatRoomRepository.save(memberChatRoom);

            clubApplication.changeStatus(ClubApplicationType.APPROVED);
        } else if (processType.equals(ProcessType.REJECT)) {
            clubApplication.changeStatus(ClubApplicationType.REJECTED);
        }



        clubApplicationRepository.save(clubApplication);
    }

    public void cancelApplication(Long clubId, Long memberId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("클럽을 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        // Todo: 없는지, 1개만 있는지 체크해야 하나?
        List<ClubApplication> clubApplications = clubApplicationRepository.findByMemberIdAndClubId(memberId, clubId);
        clubApplications
                .forEach(clubApplication -> {
                            if (clubApplication.getStatus().equals(ClubApplicationType.PENDING)) {
                                clubApplication.changeStatus(ClubApplicationType.CANCELED);
                            }
                            clubApplicationRepository.save(clubApplication);
                        }
                );
    }
}
