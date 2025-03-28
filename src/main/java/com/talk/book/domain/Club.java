package com.talk.book.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "club")
@Getter
@Setter
@Builder
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false)
    private Member host;

    @Column(length = 20, nullable = false)
    private String isbn13;

    @Column(length = 20, nullable = false)
    private String bookTitle;

    @Column(length = 255, nullable = false)
    private String name;

//    @Column(length = 255, nullable = false)
//    private String location;

    @Column(nullable = false)
    private int maxParticipants;

    @Column(nullable = false)
    @Builder.Default
    private int currentParticipant = 1;

    @Column(length = 20, nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private int duration;

    @Column(columnDefinition = "TEXT")
    private String clubDescription;

    @Column(length = 255)
    private String clubImage;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}

