package com.talk.book.service;

import com.talk.book.domain.Club;
import com.talk.book.domain.FavoriteClub;
import com.talk.book.domain.Member;
import com.talk.book.dto.FavoriteClubResponse;
import com.talk.book.repository.ClubRepository;
import com.talk.book.repository.FavoriteClubRepository;
import com.talk.book.repository.MemberRepository;
import com.talk.book.security.CustomException;
import com.talk.book.security.ErrorCode;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteClubService {

    private final FavoriteClubRepository favoriteClubRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;


    public boolean isFavoriteClub(Long memberId, Long clubId) {
        return favoriteClubRepository.existsByMemberIdAndClubId(memberId, clubId);
    }

    @Transactional
    public void addFavoriteClub(Long memberId, Long clubId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));

        if (favoriteClubRepository.existsByMemberAndClub(member, club)) {
            throw new CustomException(ErrorCode.ALREADY_FAVORITED);
        }

        favoriteClubRepository.save(FavoriteClub.builder()
                .member(member)
                .club(club)
                .build());
    }

    @Transactional
    public void removeFavoriteClub(Long memberId, Long clubId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND));
        FavoriteClub favorite = favoriteClubRepository.findByMemberAndClub(member, club)
                .orElseThrow(() -> new CustomException(ErrorCode.FAVORITE_NOT_FOUND));

        favoriteClubRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteClubResponse> getFavoriteClubs(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<FavoriteClub> favorites = favoriteClubRepository.findAllByMember(member);
        return FavoriteClubResponse.from(favorites);
    }
}
