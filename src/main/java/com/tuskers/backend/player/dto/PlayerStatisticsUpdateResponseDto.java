package com.tuskers.backend.player.dto;

import com.tuskers.backend.player.enums.Tournament;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlayerStatisticsUpdateResponseDto {
    private Integer playerId;

    private Integer playerStatisticsId;

    private Tournament tournament;

}
