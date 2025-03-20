package com.talk.book.service;

import com.talk.book.domain.Club;
import com.talk.book.domain.Member;
import com.talk.book.dto.*;
import com.talk.book.repository.ClubRepository;
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

    public Club createClub(ClubRequest request, Long hostId) {
        Member host = memberRepository.findById(hostId)
                .orElseThrow(() -> new IllegalArgumentException("호스트를 찾을 수 없습니다."));

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

        return clubRepository.save(club);
    }

    public ClubResponseDTO getClubsByIsbn(String isbn13) {
        List<Club> clubs = clubRepository.findByIsbn13(isbn13);

        List<ClubDTO> clubDTOs = clubs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ClubResponseDTO(clubDTOs.size(), clubDTOs);
    }

    public ClubDTO getClubDetailById(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("클럽을 찾을 수 없습니다."));

        return convertToDTO(club);
    }

    public ParticipantListDTO getClubParticipants(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("클럽을 찾을 수 없습니다."));

        List<ParticipantDTO> participants = club.getParticipants().stream()
                .map(member -> new ParticipantDTO(
                        club.getHost().getId().equals(member.getId()),
                        member.getId(),
                        member.getNickname()
                ))
                .collect(Collectors.toList());

        return new ParticipantListDTO(participants);
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

    public void deleteClub(Long clubId) {
        clubRepository.deleteById(clubId);
    }
}