package com.tuskers.backend.player.service;

import com.tuskers.backend.player.dto.PlayerStatisticsDto;
import com.tuskers.backend.player.dto.PlayerStatisticsUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlayerStatisticsService {
    PlayerStatisticsDto getIndividualPlayerStatistics(Integer playerId, String tournament);

    Page<PlayerStatisticsDto> getPlayerStatisticsList(String tournament, Pageable pageable);

    com.tuskers.backend.player.entity.PlayerStatistics updatePlayerStatistics(
            Integer playerId,
            String tournament,
            Integer playerStatisticsId,
            PlayerStatisticsUpdateRequestDto statistics);

    void deletePlayerStatisticsById(Integer id);
}
