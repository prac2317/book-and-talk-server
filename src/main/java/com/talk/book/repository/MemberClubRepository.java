package com.talk.book.repository;

import com.talk.book.domain.MemberClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberClubRepository extends JpaRepository<MemberClub, Long> {
    List<MemberClub> findByClubId(Long clubId);
}
