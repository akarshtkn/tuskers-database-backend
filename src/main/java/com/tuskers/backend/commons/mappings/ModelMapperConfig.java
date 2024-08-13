package com.tuskers.backend.commons.mappings;

import com.tuskers.backend.player.dto.PlayerStatisticsUpdateResponseDto;
import com.tuskers.backend.player.entity.PlayerStatistics;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        configurePlayerStatisticsToDtoMapping(modelMapper);

        return new ModelMapper();
    }

    public void configurePlayerStatisticsToDtoMapping(ModelMapper modelMapper) {
        modelMapper.typeMap(PlayerStatistics.class, PlayerStatisticsUpdateResponseDto.class)
                .addMappings(mapper ->
                        mapper.map(src -> src.getPlayer().getId(), PlayerStatisticsUpdateResponseDto::setPlayerId));
    }
}
