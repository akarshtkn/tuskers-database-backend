package com.tuskers.backend.player.controller;

import com.tuskers.backend.player.dto.*;
import com.tuskers.backend.player.entity.Player;
import com.tuskers.backend.player.enums.District;
import com.tuskers.backend.player.service.PlayerService;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
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

    @PostMapping("/add")
    public ResponseEntity<CreatePlayerResponseDto> createPlayer(@RequestBody CreatePlayerRequestDto request) {
        Player createdPlayer = playerService.createPlayer(request.getUsername(), request.getGameId(),
                request.getDistrict());
        CreatePlayerResponseDto playerDto = modelMapper.map(createdPlayer, CreatePlayerResponseDto.class);
        return new ResponseEntity<>(playerDto, HttpStatus.CREATED);
    }

    @PostMapping("/add/check")
    public ResponseEntity<Boolean> checkDuplicateUsername(@RequestParam(required = false) String username,
                                                          @RequestParam(required = false) String gameId) {
        if (username == null || username.isBlank()) {
            return new ResponseEntity<>(playerService.checkForDuplicateGameId(gameId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(playerService.checkUsernameAlreadyExist(username), HttpStatus.OK);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<CreatePlayerResponseDto> updatePlayer(@RequestParam(required = true) Integer playerId,
                                                                @RequestBody UpdatePlayerRequestDto request) {
        Player updatedPlayer = playerService.updatePlayer(playerId, request.getGameId(), request.getDistrict());
        CreatePlayerResponseDto playerDto = modelMapper.map(updatedPlayer, CreatePlayerResponseDto.class);
        return new ResponseEntity<>(playerDto, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<PlayerResponseDto> getPlayers(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "8") Integer pageSize,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String district) {

        Page<Player> players;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("username").ascending());
        District districtEnum = null;

        if (district.equals("ALL") || district.isBlank() || "undefined".equalsIgnoreCase(district)) {
            district = null;
        }

        if (district!=null){
            try {
                districtEnum = District.valueOf(district);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid district selected : " + district);
            }
        }

        if(filter == null || filter.isBlank()){
            if(district == null){
                players = playerService.getAllPlayers(pageable);
            }else {
                players = playerService.getPlayersByDistrict(districtEnum, pageable);
            }
        }else{
            if(district == null){
                players = playerService.getPlayersByUsername(filter, pageable);
            }else {
                players = playerService.getPlayersByUsernameAndDistrict(filter, districtEnum, pageable);
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
    public ResponseEntity<HttpStatus> deletePlayer(@RequestParam(required = true) Integer playerId) {
        playerService.deletePlayer(playerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
