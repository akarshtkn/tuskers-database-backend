package com.tuskers.backend.commons.mappings;

import com.tuskers.backend.player.dto.PlayerStatisticsUpdateResponseDto;
import com.tuskers.backend.player.entity.PlayerStatistics;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomMappings {

    private final ModelMapper modelMapper;

    public PlayerStatisticsUpdateResponseDto convertToPlayerStatisticsUpdateResponseDto(
            PlayerStatistics playerStatistics) {
        return modelMapper.map(playerStatistics, PlayerStatisticsUpdateResponseDto.class);
    }
}
