package com.tuskers.backend.service;

import com.tuskers.backend.entity.Player;
import com.tuskers.backend.enums.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerService {
    Player createPlayer(String username, String gameId, District district);

    Player updatePlayer(Integer playerId, String gameId, District district);

    Page<Player> getAllPlayers(Pageable pageable);

    Page<Player> getPlayersByUsername(String filter, Pageable pageable);

    Page<Player> getPlayersByDistrict(District district, Pageable pageable);

    Page<Player> getPlayersByUsernameAndDistrict(String filter, District district, Pageable pageable);

    void deletePlayer(Integer playerId);
}
