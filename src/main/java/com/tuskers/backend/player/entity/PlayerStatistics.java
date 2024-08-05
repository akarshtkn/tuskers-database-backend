package com.tuskers.backend.player.entity;

import com.tuskers.backend.player.enums.Tournament;
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
@Table(name = "player_statistics")
public class PlayerStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "player_statistics_id")
    private Integer playerStatisticsId;

    @Column(name = "played")
    private Integer played;

    @Column(name = "win")
    private Integer win;

    @Column(name = "draw")
    private Integer draw;

    @Column(name = "loss")
    private Integer loss;

    @Column(name = "tournament")
    private Tournament tournament;

    @Column(name = "points")
    private Long points;

    @ManyToOne
    @JoinColumn(name = "player_id_fk", referencedColumnName = "player_id")
    private Player player;

    public PlayerStatistics(Integer played, Integer win, Integer draw, Integer loss, Tournament tournament,
                            Long points, Player player) {
        this.played = played;
        this.win = win;
        this.draw = draw;
        this.loss = loss;
        this.tournament = tournament;
        this.points = points;
        this.player = player;
    }
}
