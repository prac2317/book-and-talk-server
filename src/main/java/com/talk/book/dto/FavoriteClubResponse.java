package com.talk.book.dto;

import com.talk.book.domain.FavoriteClub;
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

    public static ClubListItemDTO toClubListItemDTO(FavoriteClubResponse response) {
        return new ClubListItemDTO(
                response.getClubId(),
                response.getBookTitle(),
                response.getName(),
                response.getCurrentParticipants(),
                response.getMaxParticipants(),
                response.getStatus(),
                response.getStartDate()
        );
    }

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
                        .build())
                .collect(Collectors.toList());
    }
}