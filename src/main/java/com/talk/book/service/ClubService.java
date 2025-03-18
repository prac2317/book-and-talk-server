package com.talk.book.service;

import com.talk.book.domain.Club;
import com.talk.book.domain.Member;
import com.talk.book.dto.ClubRequest;
import com.talk.book.repository.ClubRepository;
import com.talk.book.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;

    public List<Club> getClubsByIsbn(String isbn13) {
        if (isbn13 == null || isbn13.isEmpty()) {
            throw new IllegalArgumentException("ISBN13을 입력해야 합니다.");
        }

        List<Club> clubs = clubRepository.findByIsbn13(isbn13);
        if (clubs.isEmpty()) {
            throw new IllegalArgumentException("해당 ISBN13을 가진 클럽이 없습니다.");
        }
        return clubs;
    }

    public Optional<Club> getClubById(Long clubId) {
        return clubRepository.findById(clubId);
    }

    public Club createClub(ClubRequest request, Long hostId) {
        Member host = memberRepository.findById(hostId)
                .orElseThrow(() -> new IllegalArgumentException("호스트를 찾을 수 없습니다."));

        Club club = new Club();
        club.setHost(host);
        club.setIsbn13(request.getIsbn13());
        club.setBookTitle(request.getBookTitle());
        club.setName(request.getName());
//        club.setLocation(request.getLocation());
        club.setMaxParticipants(request.getMaxParticipants());
//        club.setStatus(request.getStatus());
        club.setStartDate(request.getStartDate());
        club.setDuration(request.getDuration());
        club.setClubDescription(request.getClubDescription());
//        club.setClubImage(request.getClubImage());

        return clubRepository.save(club);
    }

    public void deleteClub(Long clubId) {
        clubRepository.deleteById(clubId);
    }
}
