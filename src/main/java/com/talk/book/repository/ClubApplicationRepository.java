package com.talk.book.repository;

import com.talk.book.domain.ClubApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubApplicationRepository extends JpaRepository<ClubApplication, Long> {
}
