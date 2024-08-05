package com.tuskers.backend.player.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlayerStatisticsResponseDto {
    private List<PlayerStatisticsDto> playerStatisticsDtoResponse;

    private long totalPlayers;

    private int currentPage;

    private int totalPages;
}
