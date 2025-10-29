package com.talk.book.dto;

import com.talk.book.domain.FavoriteClub;
import com.talk.book.repository.ClubRepository;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class FavoriteClubResponse {
    private Long clubId;
    private String bookTitle;
    private String name;
    private int currentParticipants;
    private int maxParticipants;
    private String status;
    private LocalDateTime startDate;
    private String clubImage;

    private ClubRepository clubRepository;

    public static List<FavoriteClubResponse> from(List<FavoriteClub> favorites) {
        return favorites.stream()
                .map(fav -> FavoriteClubResponse.builder()
                        .clubId(fav.getClub().getId())
                        .bookTitle(fav.getClub().getBookTitle())
                        .name(fav.getClub().getName())
                        .currentParticipants(fav.getClub().getCurrentParticipant())
                        .maxParticipants(fav.getClub().getMaxParticipants())
                        .status(fav.getClub().getStatus())
                        .startDate(fav.getClub().getStartDate())
                        .clubImage(fav.getClub().getClubImage())
                        .build())
                .collect(Collectors.toList());
    }
}