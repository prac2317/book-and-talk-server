package com.talk.book.service;

import com.talk.book.domain.Club;
import com.talk.book.domain.Member;
import com.talk.book.domain.MemberClub;
import com.talk.book.dto.*;
import com.talk.book.enumerate.ClubMemberRelationType;
import com.talk.book.repository.ClubRepository;
import com.talk.book.repository.MemberClubRepository;
import com.talk.book.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final MemberClubRepository memberClubRepository;

    public Club createClub(ClubRequest request, Long hostId) {
        Member host = memberRepository.findById(hostId)
                .orElseThrow(() -> new IllegalArgumentException("호스트를 찾을 수 없습니다."));

        if (request.getMaxParticipants() < 1) {
            throw new IllegalArgumentException("최대 참가자는 1명 이상이어야 합니다.");
        }

        Club club = Club.builder()
                .host(host)
                .name(request.getName())
                .bookTitle(request.getBookTitle())
                .isbn13(request.getIsbn13())
                .startDate(request.getStartDate())
                .duration(request.getDuration())
                .maxParticipants(request.getMaxParticipants())
                .currentParticipant(1)
                .status("모집중")
                .clubDescription(request.getClubDescription())
                .clubImage("")
                .createdAt(LocalDateTime.now())
                .build();

        Club savedClub = clubRepository.save(club);

        MemberClub memberClub = new MemberClub();
        memberClub.setMember(host);
        memberClub.setClub(savedClub);
        memberClub.setHost(true);
        memberClubRepository.save(memberClub);

        return savedClub;
    }


    public MemberListDTO getClubMembers(Long clubId) {
        List<MemberClub> members = memberClubRepository.findByClubId(clubId);

        List<MemberDTO> memberDTOs = members.stream()
                .map(memberClub -> new MemberDTO(
                        memberClub.isHost(),
                        memberClub.getMember().getId(),
                        memberClub.getMember().getNickname()
                ))
                .collect(Collectors.toList());

        return new MemberListDTO(memberDTOs);
    }

    public ClubMemberRelationDTO getRelation(Long clubId, Long hostId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("클럽을 찾을 수 없습니다."));

        // Todo: 1. 호스트와 사용자 일치하는지 확인
        if (club.getHost().getId().equals(hostId)) {
            return new ClubMemberRelationDTO(ClubMemberRelationType.HOST);
        }

        // Todo: 2. 사용자가 클럽의 멤버인지 확인
        List<MemberClub> members = memberClubRepository.findByClubId(clubId);
        for (MemberClub memberClub : members) {
            if (memberClub.getMember().getId() == hostId) {
                return new ClubMemberRelationDTO(ClubMemberRelationType.MEMBER);
            }
        }

        // Todo: 3. 사용자가 참가 신청 상태인지 확인하기

        // Todo: 4. 아무관계도 아니면 아니라고 반환
        return new ClubMemberRelationDTO(ClubMemberRelationType.NONE);

    }

    public ClubResponseDTO getClubsByIsbn(String isbn13) {
        List<Club> clubs = clubRepository.findByIsbn13(isbn13);

        List<ClubListItemDTO> clubListItemDTOs = clubs.stream()
                .map(this::convertToListItemDTO)
                .collect(Collectors.toList());

        return new ClubResponseDTO(clubListItemDTOs.size(), clubListItemDTOs);
    }

    public ClubDTO getClubDetailById(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("클럽을 찾을 수 없습니다."));

        return convertToDTO(club);
    }

    public ClubDTO convertToDTO(Club club) {
        return new ClubDTO(
                club.getId(),
                club.getName(),
                club.getBookTitle(),
                club.getMaxParticipants(),
                club.getCurrentParticipant(),
                club.getStartDate(),
                club.getDuration(),
                club.getStatus(),
                club.getClubDescription(),
                club.getIsbn13(),
                club.getCreatedAt()
        );
    }

    public ClubListItemDTO convertToListItemDTO(Club club) {
        return new ClubListItemDTO(
                club.getId(),
                club.getBookTitle(),
                club.getName(),
                club.getCurrentParticipant(),
                club.getMaxParticipants(),
                club.getStatus(),
                club.getStartDate()
        );
    }

    public void deleteClub(Long clubId) {
        clubRepository.deleteById(clubId);
    }
}