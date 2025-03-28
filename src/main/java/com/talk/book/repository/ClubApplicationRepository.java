package com.talk.book.repository;

import com.talk.book.domain.ClubApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubApplicationRepository extends JpaRepository<ClubApplication, Long> {
    List<ClubApplication> findByMemberIdAndClubId(Long memberId, Long clubId);
    List<ClubApplication> findByClubId(Long clubId);
}
