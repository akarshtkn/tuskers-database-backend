package com.tuskers.backend.player.dto;

import com.tuskers.backend.player.enums.District;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRequest {

    @NotBlank(message = "username should not be blank")
    private String username;

    @NotBlank(message = "game id should not be blank")
    private String gameId;

    @NotBlank(message = "district should not be blank")
    private District district;
}
