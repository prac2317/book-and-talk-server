package com.talk.book.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat")
@Getter
@Setter
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @JoinColumn(name = "sender_id")
    @OneToOne
    private Member sender;

    @Column(length = 200, nullable = false)
    private String content;

    @JoinColumn(name = "room_id")
    @ManyToOne
    private ChatRoom room;

    @Column
    private LocalDateTime createdAt;

}
