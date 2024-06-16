package com.tuskers.backend.player.service.serviceimpl;

import com.tuskers.backend.player.entity.Player;
import com.tuskers.backend.player.enums.District;
import com.tuskers.backend.player.repository.PlayerRepository;
import com.tuskers.backend.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public Player createPlayer(String username, String gameId, District district) {
        if(!Arrays.asList(District.values()).contains(district)){
            throw new IllegalArgumentException("Invalid district selected : " + district);
        }
        Player player = new Player(username, gameId, district);
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
        if(!Arrays.asList(District.values()).contains(district)){
            throw new IllegalArgumentException("Invalid district selected : " + district);
        }
        return playerRepository.filterByDistrict(district, pageable);
    }

    @Override
    public Page<Player> getPlayersByUsernameAndDistrict(String filter, District district, Pageable pageable) {
        if(!Arrays.asList(District.values()).contains(district)){
            throw new IllegalArgumentException("Invalid district selected : " + district);
        }
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
        return playerRepository.countByUsername(username) != 0;
    }

    @Override
    public Boolean checkForDuplicateGameId(String gameId) {
        return playerRepository.countByGameId(gameId) != 0;
    }

}
