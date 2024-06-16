package com.tuskers.backend.player.entity;

import com.tuskers.backend.player.enums.District;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "player_id")
    private Integer id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "game_id", unique = true)
    private String gameId;

    @Column(name = "district")
    private District district;

    public Player(String username, String gameId, District district) {
        this.username = username;
        this.gameId = gameId;
        this.district = district;
    }
}
