package com.tuskers.backend.controller;

import com.tuskers.backend.dto.CreatePlayerRequestDto;
import com.tuskers.backend.dto.CreatePlayerResponseDto;
import com.tuskers.backend.dto.PlayerResponseDto;
import com.tuskers.backend.dto.UpdatePlayerRequestDto;
import com.tuskers.backend.entity.Player;
import com.tuskers.backend.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<CreatePlayerResponseDto> createPlayer(@RequestBody CreatePlayerRequestDto request) {
        Player createdPlayer = playerService.createPlayer(request.getUsername(), request.getGameId(),
                request.getDistrict());
        CreatePlayerResponseDto playerDto = modelMapper.map(createdPlayer, CreatePlayerResponseDto.class);
        return new ResponseEntity<>(playerDto, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<CreatePlayerResponseDto> updatePlayer(@RequestParam(required = true) Integer playerId,
                                                                @RequestBody UpdatePlayerRequestDto request) {
        Player updatedPlayer = playerService.updatePlayer(playerId, request.getGameId(), request.getDistrict());
        CreatePlayerResponseDto playerDto = modelMapper.map(updatedPlayer, CreatePlayerResponseDto.class);
        return new ResponseEntity<>(playerDto, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<List<PlayerResponseDto>> getPlayers(@RequestParam(required = false) String filter) {
        List<Player> players = new ArrayList<>();
        if(filter == null || filter.isBlank()){
            players = playerService.getAllPlayers();
        } else{
            players = playerService.getPlayersByUsername(filter);
        }
        List<PlayerResponseDto> playerResponseDtoList = players.stream()
                .map(player -> modelMapper.map(player, PlayerResponseDto.class))
                .toList();
        return new ResponseEntity<>(playerResponseDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deletePlayer(@RequestParam(required = true) Integer playerId) {
        playerService.deletePlayer(playerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
