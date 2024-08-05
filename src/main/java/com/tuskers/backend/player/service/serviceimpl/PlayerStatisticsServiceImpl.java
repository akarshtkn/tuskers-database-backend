package com.tuskers.backend.player.service.serviceimpl;

import com.tuskers.backend.commons.exceptions.BadRequestException;
import com.tuskers.backend.commons.exceptions.InvalidArgumentException;
import com.tuskers.backend.player.dto.PlayerStatisticsDto;
import com.tuskers.backend.player.dto.PlayerStatisticsUpdateRequestDto;
import com.tuskers.backend.player.entity.Player;
import com.tuskers.backend.player.entity.PlayerStatistics;
import com.tuskers.backend.player.enums.Tournament;
import com.tuskers.backend.player.repository.PlayerStatisticsRepository;
import com.tuskers.backend.player.service.PlayerService;
import com.tuskers.backend.player.service.PlayerStatisticsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlayerStatisticsServiceImpl implements PlayerStatisticsService {

    private final PlayerService playerService;
    private final PlayerStatisticsRepository repository;

    Logger logger = LoggerFactory.getLogger(PlayerStatisticsServiceImpl.class);

    @Override
    public PlayerStatisticsDto getIndividualPlayerStatistics(Integer playerId, String tournament) {
        if(!playerService.checkPlayerExist(playerId)) {
            throw new BadRequestException("Player does not exist with id: " + playerId);
        }
        Tournament tournamentFilter = checkAndExtractTournament(tournament);
        return repository.getIndividualPlayerStatisticsByIdAndTournament(playerId, tournamentFilter);
    }

    @Override
    public Page<PlayerStatisticsDto> getPlayerStatisticsList(String tournament, Pageable pageable) {
        if(!tournament.equals("ALL")) {
            Tournament tournamentFilter = checkAndExtractTournament(tournament);
            logger.info("Tournament filter :{}", tournamentFilter);

            return repository.getListByTournament(tournamentFilter, pageable);
        } else {
            return repository.getOverallStatisticList(pageable);
        }
    }

    @Override
    public PlayerStatistics updatePlayerStatistics(
            Integer playerId,
            String tournament,
            Integer playerStatisticsId,
            PlayerStatisticsUpdateRequestDto statistics) {
        logger.info("Invoking function to update player statistics");

        checkStatisticsDataValidity(statistics);

        Player player = playerService.findPlayerById(playerId);
        if(playerStatisticsId != null) {
            com.tuskers.backend.player.entity.PlayerStatistics playerStatistics = repository.findById(playerStatisticsId)
                    .orElseThrow(() -> new BadRequestException("Invalid player statistics id :" + playerStatisticsId));
            if(!Objects.equals(playerStatistics.getPlayer().getId(), player.getId())) {
                logger.error("Passed player statistics id :{} does not match with corresponding player id :{}", 
                        playerId, playerStatisticsId);
                throw new InvalidArgumentException("Passed player statistics id does not match with player id");
            }
            int draw = statistics.getPlayed() - statistics.getWin() - statistics.getLoss();

            com.tuskers.backend.player.entity.PlayerStatistics updatedPlayerStatistics = new com.tuskers.backend.player.entity.PlayerStatistics(
                    playerStatisticsId,
                    playerStatistics.getPlayed() + statistics.getPlayed(),
                    playerStatistics.getWin() + statistics.getWin(),
                    playerStatistics.getDraw() + draw,
                    playerStatistics.getLoss() + statistics.getLoss(),
                    playerStatistics.getTournament(),
                    playerStatistics.getPoints() + (20L * statistics.getWin()) + (10L * draw),
                    playerStatistics.getPlayer()
            );
            return repository.save(updatedPlayerStatistics);
        } else {
            Tournament tournamentFilter = checkAndExtractTournament(tournament);
            int draw = statistics.getPlayed() - statistics.getWin() - statistics.getLoss();

            com.tuskers.backend.player.entity.PlayerStatistics newPlayerStatistics = new com.tuskers.backend.player.entity.PlayerStatistics(
                    statistics.getPlayed(),
                    statistics.getWin(),
                    draw,
                    statistics.getLoss(),
                    tournamentFilter,
                    20L * statistics.getWin() + 10L * draw,
                    player
            );

            return repository.save(newPlayerStatistics);
        }
    }

    @Override
    public void deletePlayerStatisticsById(Integer id) {
        if(repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            logger.error("No player found with id :{}", id);
            throw new BadRequestException("No player found with id: " + id);
        }
    }

    private Tournament checkAndExtractTournament(String tournament) {
        logger.info("Checking and extracting if tournament filter is valid");

        Tournament tournamentFilter;
        try {
            tournamentFilter = Tournament.valueOf(tournament);
        } catch (Exception e) {
            logger.error("Invalid tournament filter passed :{}", tournament);
            throw new BadRequestException("Invalid tournament filter passed " + tournament);
        }

        return tournamentFilter;
    }

    private void checkStatisticsDataValidity(PlayerStatisticsUpdateRequestDto stats) {
        if(!(stats.getPlayed() >= stats.getWin() + stats.getLoss())) {
            logger.error("Player statistics input is logically wrong.\n" +
                    "Played: {}\nWin: {}\nLoss: {}", stats.getPlayed(), stats.getWin(), stats.getLoss());
            throw new BadRequestException("Player statistics input is logically wrong");
        }
    }
}
