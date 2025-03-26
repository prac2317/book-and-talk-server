package com.talk.book.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Table(name="club_application")
@Builder
@Getter
public class ClubApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_application_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(length = 200, nullable = false)
    private String QuestionAnswer;

    @Column(nullable = false)
    private Boolean isCompleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
