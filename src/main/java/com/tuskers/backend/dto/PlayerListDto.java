package com.tuskers.backend.dto;

import com.tuskers.backend.enums.District;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerListDto {
    private Integer id;

    private String username;

    private String gameId;

    private District district;
}
