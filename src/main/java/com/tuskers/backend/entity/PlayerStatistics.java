package com.tuskers.backend.entity;

import com.tuskers.backend.enums.Tournament;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
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

    @Column(name = "appearance")
    private Integer appearance;

    @Column(name = "win")
    private Integer win;

    @Column(name = "loss")
    private Integer loss;

    @Column(name = "goal_for")
    private Integer goalFor;

    @Column(name = "goal_against")
    private Integer goalAgainst;

    @Column(name = "clean_sheet")
    private Integer cleanSheet;

    @Column(name = "tournament")
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "player_id_fk", referencedColumnName = "player_id")
    private Player player;
}
