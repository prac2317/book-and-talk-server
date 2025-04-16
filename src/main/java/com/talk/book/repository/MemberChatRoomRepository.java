package com.talk.book.repository;

import com.talk.book.domain.MemberChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoom, Long> {
}
