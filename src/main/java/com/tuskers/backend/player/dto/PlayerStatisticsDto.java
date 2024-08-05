package com.tuskers.backend.player.dto;

public interface PlayerStatisticsDto {

    Integer getPlayerId();

    Integer getPlayerStatisticsId();

    String getName();

    Integer getPlayed();

    Integer getWin();

    Integer getDraw();

    Integer getLoss();

    Long getPoints();

}
