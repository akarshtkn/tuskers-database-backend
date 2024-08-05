package com.tuskers.backend.player.repository;

import com.tuskers.backend.player.entity.Player;
import com.tuskers.backend.player.enums.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    @Query(value = "SELECT * FROM player WHERE LOWER(username) LIKE LOWER(CONCAT(:filter, '%'))", nativeQuery = true)
    Page<Player> filterByUsername(String filter, Pageable pageable);

    @Query(value = "SELECT * FROM player WHERE district = :district", nativeQuery = true)
    Page<Player> filterByDistrict(District district, Pageable pageable);

    @Query(value = "SELECT * FROM player WHERE district = :district AND LOWER(username) LIKE LOWER(CONCAT(:filter, '%'))", nativeQuery = true)
    Page<Player> filterByUsernameAndDistrict(String filter, District district, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM player WHERE LOWER(username) = LOWER(:username)", nativeQuery = true)
    int countByUsername(String username);

    @Query(value = "SELECT COUNT(*) FROM player WHERE game_id = :gameId", nativeQuery = true)
    int countByGameId(String gameId);
}
