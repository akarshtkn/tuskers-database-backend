package com.tuskers.backend.service;

import com.tuskers.backend.entity.Player;
import com.tuskers.backend.enums.District;

import java.util.List;

public interface PlayerService {
    Player createPlayer(String username, String gameId, District district);

    Player updatePlayer(Integer playerId, String gameId, District district);

    List<Player> getAllPlayers();

    List<Player> getPlayersByUsername(String filter);

    void deletePlayer(Integer playerId);
}
