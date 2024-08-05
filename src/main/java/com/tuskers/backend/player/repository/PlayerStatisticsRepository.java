package com.tuskers.backend.player.repository;

import com.tuskers.backend.player.dto.PlayerStatisticsDto;
import com.tuskers.backend.player.enums.Tournament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerStatisticsRepository extends JpaRepository<com.tuskers.backend.player.entity.PlayerStatistics, Integer> {

    @Query(value = "SELECT ps.player_statistics_id AS playerStatisticsId, p.username AS name, ps.played AS played, "
            + "ps.win AS win, ps.draw AS draw, ps.loss AS loss, ps.points AS points "
            + "FROM player_statistics ps "
            + "INNER JOIN player p ON p.player_id = ps.player_id_fk "
            + "WHERE tournament = :tournamentFilter", nativeQuery = true)
    Page<PlayerStatisticsDto> getListByTournament(Tournament tournamentFilter, Pageable pageable);

    @Query(value = "SELECT p.player_id AS playerId, p.username AS name, SUM(ps.played) AS played, SUM(ps.win) AS win, "
            + "SUM(ps.draw) AS draw, SUM(ps.loss) AS loss, SUM(ps.points) AS points "
            + "FROM player_statistics ps "
            + "INNER JOIN player p ON p.player_id = ps.player_id_fk "
            + "GROUP BY p.username, p.player_id", nativeQuery = true)
    Page<PlayerStatisticsDto> getOverallStatisticList(Pageable pageable);

    @Query(value = "SELECT p.player_id AS playerId, p.username AS name, ps.player_statistics_id AS playerStatisticsId, "
            + "ps.played AS played, ps.win AS win, ps.draw AS draw, ps.loss AS loss, ps.points AS points "
            + "FROM player_statistics ps "
            + "INNER JOIN player p ON p.player_id = ps.player_id_fk "
            + "WHERE ps.tournament = :tournament AND p.player_id = :playerId", nativeQuery = true)
    PlayerStatisticsDto getIndividualPlayerStatisticsByIdAndTournament(Integer playerId, Tournament tournament);
}
