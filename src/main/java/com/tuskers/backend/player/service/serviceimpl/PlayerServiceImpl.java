package com.tuskers.backend.player.service.serviceimpl;

import com.tuskers.backend.commons.exceptions.BadRequestException;
import com.tuskers.backend.player.controller.PlayerController;
import com.tuskers.backend.player.entity.Player;
import com.tuskers.backend.player.enums.District;
import com.tuskers.backend.player.repository.PlayerRepository;
import com.tuskers.backend.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Override
    public Player addPlayer(String username, String gameId, District district) {
        logger.info("Executing business logic to add a player");
        logger.info("Executing business logic to validate the field district");
        if(!Arrays.asList(District.values()).contains(district)){
            throw new BadRequestException("Invalid district selected, value = " + district);
        }

        Player player = new Player(username, gameId, district);

        logger.info("Saving player to repository");
        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(Integer playerId, String gameId, District district) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("No player exist with player_id : " + playerId ));
        if(!gameId.isBlank() && !gameId.equals(player.getGameId())){
            player.setGameId(gameId);
        }
        if(!Arrays.asList(District.values()).contains(district) && player.getDistrict() != district){
            player.setDistrict(district);
        }
        return playerRepository.save(player);
    }

    @Override
    public Page<Player> getAllPlayers(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    @Override
    public Page<Player> getPlayersByUsername(String filter, Pageable pageable) {
        return playerRepository.filterByUsername(filter, pageable);
    }

    @Override
    public Page<Player> getPlayersByDistrict(District district, Pageable pageable) {
        return playerRepository.filterByDistrict(district, pageable);
    }

    @Override
    public Page<Player> getPlayersByUsernameAndDistrict(String filter, District district, Pageable pageable) {
        return playerRepository.filterByUsernameAndDistrict(filter, district, pageable);
    }

    @Override
    public void deletePlayer(Integer playerId) {
        playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("No player exist with player_id : " + playerId));
        playerRepository.deleteById(playerId);
    }

    @Override
    public Boolean checkUsernameAlreadyExist(String username) {
        logger.info("Executing business logic to check for duplicate Username");
        return playerRepository.countByUsername(username) != 0;
    }

    @Override
    public Boolean checkForDuplicateGameId(String gameId) {
        logger.info("Executing business logic to check for duplicate Game Id");
        return playerRepository.countByGameId(gameId) != 0;
    }

    @Override
    public boolean checkPlayerExist(Integer playerId) {
        logger.info("Checking if player exist with id :{}", playerId);
        return playerRepository.existsById(playerId);
    }

    @Override
    public Player findPlayerById(Integer playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new BadRequestException("Player does not exist with id " + playerId));
    }

}
