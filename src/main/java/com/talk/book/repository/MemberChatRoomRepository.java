package com.talk.book.repository;

import com.talk.book.domain.MemberChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoom, Long> {
    List<MemberChatRoom> findByMemberId(Long memberId);
    List<MemberChatRoom> findByChatRoomId(Long ChatRoomId);
}
