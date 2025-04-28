package com.talk.book.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chatroom")
@Getter
@Setter
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    @JoinColumn(name = "club_id")
    @OneToOne
    private Club club;

    @JoinColumn(name = "chat_id")
    @OneToOne
    private Chat recentChat;

    @Column
    private String name;

    @Column
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
