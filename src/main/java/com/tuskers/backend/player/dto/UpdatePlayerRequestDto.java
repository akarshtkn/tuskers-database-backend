package com.tuskers.backend.player.dto;

import com.tuskers.backend.player.enums.District;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePlayerRequestDto {
    private String fullName;
    private String gameId;
    private District district;
}
