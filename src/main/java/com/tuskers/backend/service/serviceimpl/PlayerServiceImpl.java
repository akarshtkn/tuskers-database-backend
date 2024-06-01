package com.tuskers.backend.service.serviceimpl;

import com.tuskers.backend.entity.Player;
import com.tuskers.backend.enums.District;
import com.tuskers.backend.repository.PlayerRepository;
import com.tuskers.backend.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public List<Player> getPlayersByUsername(String filter) {
        return playerRepository.filterByUsername(filter);
    }

    @Override
    public void deletePlayer(Integer playerId) {
        playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("No player exist with player_id : " + playerId));
        playerRepository.deleteById(playerId);
    }

}
