package com.tuskers.backend.dto;

import com.tuskers.backend.enums.District;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlayerRequestDto {

    @NotBlank(message = "username should not be blank")
    private String username;

    @NotBlank(message = "game id should not be blank")
    private String gameId;

    private District district;
}
