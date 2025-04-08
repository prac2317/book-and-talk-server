package com.talk.book.repository;

import com.talk.book.domain.Club;
import com.talk.book.domain.FavoriteClub;
import com.talk.book.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteClubRepository extends JpaRepository<FavoriteClub, Long> {

    boolean existsByMemberAndClub(Member member, Club club);

    Optional<FavoriteClub> findByMemberAndClub(Member member, Club club);

    List<FavoriteClub> findAllByMember(Member member);

    boolean existsByMemberIdAndClubId(Long memberId, Long clubId);
}
