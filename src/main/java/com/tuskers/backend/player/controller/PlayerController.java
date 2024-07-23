package com.tuskers.backend.player.controller;

import com.tuskers.backend.commons.exceptions.BadRequestException;
import com.tuskers.backend.player.dto.*;
import com.tuskers.backend.player.entity.Player;
import com.tuskers.backend.player.enums.District;
import com.tuskers.backend.player.service.PlayerService;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final ModelMapper modelMapper;

    Logger logger = LoggerFactory.getLogger(PlayerController.class);

    @PostMapping("/add")
    public ResponseEntity<PlayerResponse> addPlayer(@RequestBody PlayerRequest request) {
        logger.info("Add player endpoint");
        Player newPlayer = playerService.addPlayer(request.getUsername(), request.getGameId(),
                request.getDistrict());

        logger.info("Player created with id : {}", newPlayer.getId());
        logger.info("Executing business logic to map the Player to PlayerResponse");
        PlayerResponse playerDto = modelMapper.map(newPlayer, PlayerResponse.class);
        return new ResponseEntity<>(playerDto, HttpStatus.CREATED);
    }

    @PostMapping("/check")
    public ResponseEntity<Object> checkDuplicateUsername(@RequestParam(required = false) String username,
                                                          @RequestParam(required = false) String gameId) {
        logger.info("Executing business logic to check for duplicates");

        if ((username == null || username.isBlank()) && (gameId == null || gameId.isBlank())) {
            logger.warn("Neither username nor gameId provided");
            return new ResponseEntity<>("Either 'username' or 'gameId' must be provided", HttpStatus.BAD_REQUEST);
        }

        logger.info("Executing business logic to check which field is present, username or gameId");
        if (username == null || username.isBlank()) {
            return new ResponseEntity<>(playerService.checkForDuplicateGameId(gameId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(playerService.checkUsernameAlreadyExist(username), HttpStatus.OK);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<PlayerResponse> updatePlayer(@RequestParam(required = true) Integer playerId,
                                                       @RequestBody UpdatePlayerRequestDto request) {
        Player updatedPlayer = playerService.updatePlayer(playerId, request.getGameId(), request.getDistrict());
        PlayerResponse playerDto = modelMapper.map(updatedPlayer, PlayerResponse.class);
        return new ResponseEntity<>(playerDto, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<PlayerResponseDto> getPlayers(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "8") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "ALL") String district) {

        logger.info("Invoking player get function");
        Page<Player> players;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("username").ascending());
        District districtEnum = null;

        if (district.equals("ALL")) {
            district = null;
        }

        if (district!=null){
            try {
                districtEnum = District.valueOf(district);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid district selected : " + district);
            }
        }

        if(username == null || username.isBlank()){
            if(district == null){
                players = playerService.getAllPlayers(pageable);
            }else {
                players = playerService.getPlayersByDistrict(districtEnum, pageable);
            }
        }else{
            if(district == null){
                players = playerService.getPlayersByUsername(username, pageable);
            }else {
                players = playerService.getPlayersByUsernameAndDistrict(username, districtEnum, pageable);
            }
        }

        List<PlayerListDto> playerListDto = players.stream()
                .map(player -> modelMapper.map(player, PlayerListDto.class))
                .toList();

        return new ResponseEntity<>(
                new PlayerResponseDto(playerListDto, players.getTotalElements(),
                        players.getNumber(), players.getTotalPages() ), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletePlayer(@RequestParam(required = true) Integer playerId) {
        playerService.deletePlayer(playerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
