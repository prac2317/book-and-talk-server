package com.talk.book.service;

import com.talk.book.domain.*;
import com.talk.book.dto.*;
import com.talk.book.enumerate.ClubMemberRelationType;
import com.talk.book.repository.*;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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
    private final ClubApplicationRepository clubApplicationRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;
    private final GeometryFactory geometryFactory;

    public Club createClub(ClubRequest request, Long memberId) {
        Member host = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("호스트를 찾을 수 없습니다."));

        if (request.getMaxParticipants() < 1) {
            throw new IllegalArgumentException("최대 참가자는 1명 이상이어야 합니다.");
        }

        Point clubLocation = geometryFactory.createPoint(
                new Coordinate(request.getLongitude(), request.getLatitude()));

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
                .address(request.getAddress())
                .location(clubLocation)
                .createdAt(LocalDateTime.now())
                .clubImage(request.getClubImage())
                .build();
        Club savedClub = clubRepository.save(club);

        ChatRoom chatRoom = ChatRoom.builder().club(savedClub)
                .recentChat(null)
                .name(savedClub.getName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        MemberChatRoom memberChatRoom = MemberChatRoom.builder().chatRoom(savedChatRoom)
                .member(host)
                .isHost(true)
                .build();
        memberChatRoomRepository.save(memberChatRoom);

        MemberClub memberClub = new MemberClub();
        memberClub.setMember(host);
        memberClub.setClub(savedClub);
        memberClub.setIsHost(true);
        memberClubRepository.save(memberClub);

        return savedClub;
    }


    public MemberListDTO getClubMembers(Long clubId) {
        List<MemberClub> members = memberClubRepository.findByClubId(clubId);

        List<MemberDTO> memberDTOs = members.stream()
                .map(memberClub -> new MemberDTO(
                        memberClub.getIsHost(),
                        memberClub.getMember().getId(),
                        memberClub.getMember().getNickname()
                ))
                .collect(Collectors.toList());

        return new MemberListDTO(memberDTOs);
    }

    public ClubMemberRelationDTO getRelation(Long clubId, Long memberId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("클럽을 찾을 수 없습니다."));

        // 방문자가 호스트인지 확인
        if (club.getHost().getId().equals(memberId)) {
            return new ClubMemberRelationDTO(ClubMemberRelationType.HOST);
        }

        // 방문자가 클럽의 멤버인지 확인
        List<MemberClub> members = memberClubRepository.findByClubId(clubId);
        for (MemberClub memberClub : members) {
            if (memberClub.getMember().getId() == memberId) {
                return new ClubMemberRelationDTO(ClubMemberRelationType.MEMBER);
            }
        }

        // 방문자가 가입 신청 상태인지 확인
        boolean isApplicant = !clubApplicationRepository.findByMemberIdAndClubId(memberId, clubId).isEmpty();
        if (isApplicant) {
            return new ClubMemberRelationDTO(ClubMemberRelationType.APPLICANT);
        }

        // 아무관계도 아님
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
                club.getCreatedAt(),
                club.getAddress(),
                club.getLatitude(),
                club.getLongitude(),
                club.getClubImage()
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
                club.getStartDate(),
                club.getClubImage()
        );
    }

    public void deleteClub(Long clubId) {
        List<MemberClub> memberClubs = memberClubRepository.findByClubId(clubId);
        for (MemberClub memberClub : memberClubs) {
            memberClubRepository.deleteById(memberClub.getId());
        }

        clubRepository.deleteById(clubId);
    }
}