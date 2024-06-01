package com.tuskers.backend.repository;

import com.tuskers.backend.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    @Query(value = "SELECT * FROM player WHERE username LIKE :filter%", nativeQuery = true)
    List<Player> filterByUsername(String filter);
}
