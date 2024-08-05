package com.tuskers.backend.player.controller;

import com.tuskers.backend.commons.mappings.CustomMappings;
import com.tuskers.backend.player.dto.PlayerStatisticsUpdateResponseDto;
import com.tuskers.backend.player.entity.PlayerStatistics;
import com.tuskers.backend.player.dto.PlayerStatisticsDto;
import com.tuskers.backend.player.dto.PlayerStatisticsResponseDto;
import com.tuskers.backend.player.dto.PlayerStatisticsUpdateRequestDto;
import com.tuskers.backend.player.service.PlayerStatisticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/player-statistics")
@RequiredArgsConstructor
public class PlayerStatisticsController {

    private static final Integer pageSize = 8;

    private final PlayerStatisticsService playerStatisticsService;
    private final CustomMappings customMappings;

    Logger logger = LoggerFactory.getLogger(PlayerStatisticsController.class);

    @GetMapping("/get/{playerId}/{tournament}")
    public ResponseEntity<PlayerStatisticsDto> getPlayerStatisticsByIdAndTournament(
            @PathVariable Integer playerId,
            @PathVariable String tournament) {

        logger.info("Invoking function to get individual player statistics by player id and tournament");

        return new ResponseEntity<>(playerStatisticsService
                .getIndividualPlayerStatistics(playerId, tournament), HttpStatus.OK);
    }

    @GetMapping("/get/{tournament}")
    public ResponseEntity<PlayerStatisticsResponseDto> getStatisticsByTournament(
            @PathVariable String tournament,
            @RequestParam(defaultValue = "0") Integer pageNo) {

        logger.info("Invoking function to get list of player statistics by tournament");

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("points").descending());

        Page<PlayerStatisticsDto> playerStatistics = playerStatisticsService
                .getPlayerStatisticsList(tournament, pageable);

        PlayerStatisticsResponseDto playerStatisticsResponseDto = new PlayerStatisticsResponseDto(
                playerStatistics.getContent(),
                playerStatistics.getTotalElements(), playerStatistics.getNumber(),
                playerStatistics.getTotalPages()
        );

        return new ResponseEntity<>(playerStatisticsResponseDto,HttpStatus.OK);
    }

    @PostMapping("/update/{playerId}/{tournament}")
    public ResponseEntity<PlayerStatisticsUpdateResponseDto> updatePlayerStatistic(
            @PathVariable Integer playerId,
            @PathVariable String tournament,
            @RequestParam(required = false) Integer playerStatisticsId,
            @Valid @RequestBody PlayerStatisticsUpdateRequestDto statistics) {

        logger.info("Invoking update player statistics function");

        PlayerStatistics updatedPlayerStatistics = playerStatisticsService
                .updatePlayerStatistics(playerId, tournament, playerStatisticsId, statistics);

        PlayerStatisticsUpdateResponseDto responseDto = customMappings
                .convertToPlayerStatisticsUpdateResponseDto(updatedPlayerStatistics);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{playerStatisticsId}")
    public ResponseEntity<Void> deletePlayerStatistics(@PathVariable Integer playerStatisticsId) {
        logger.info("Invoking delete player statistics function");

        playerStatisticsService.deletePlayerStatisticsById(playerStatisticsId);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
}
