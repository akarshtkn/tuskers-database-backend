package com.tuskers.backend.player.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResponseDto {

    private List<PlayerListDto> players;

    private long totalPlayers;

    private int currentPage;

    private int totalPages;
}
