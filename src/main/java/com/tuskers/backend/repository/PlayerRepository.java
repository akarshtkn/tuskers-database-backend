package com.tuskers.backend.repository;

import com.tuskers.backend.entity.Player;
import com.tuskers.backend.enums.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    @Query(value = "SELECT * FROM player WHERE LOWER(username) LIKE LOWER(CONCAT(:filter, '%'))", nativeQuery = true)
    Page<Player> filterByUsername(String filter, Pageable pageable);

    @Query(value = "SELECT * FROM player WHERE district= :district", nativeQuery = true)
    Page<Player> filterByDistrict(District district, Pageable pageable);

    @Query(value = "SELECT * FROM player WHERE district= :district AND LOWER(username) LIKE LOWER(CONCAT(:filter, '%'))", nativeQuery = true)
    Page<Player> filterByUsernameAndDistrict(String filter, District district, Pageable pageable);
}
