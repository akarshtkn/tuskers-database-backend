package com.tuskers.backend.player.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlayerStatisticsUpdateRequestDto {

    @NotNull
    private Integer played;

    @NotNull
    private Integer win;

    @NotNull
    private Integer loss;
}
