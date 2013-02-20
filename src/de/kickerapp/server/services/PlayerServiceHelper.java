package de.kickerapp.server.services;

import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.PlayerDoubleStats;
import de.kickerapp.server.entity.PlayerSingleStats;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.PlayerDoubleStatsDto;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.PlayerSingleStatsDto;

public class PlayerServiceHelper {

	public static PlayerDto createPlayer(Player dbPlayer, MatchType matchType) {
		final PlayerDto playerDto = new PlayerDto(dbPlayer.getLastName(), dbPlayer.getFirstName());
		playerDto.setId(dbPlayer.getKey().getId());
		playerDto.setNickName(dbPlayer.getNickName());
		playerDto.setEMail(dbPlayer.getEMail());
		playerDto.setLastMatchDate(dbPlayer.getLastMatchDate());

		switch (matchType) {
		case Single:
			setPlayerSingleStats(dbPlayer, playerDto);
			break;
		case Double:
			setPlayerDoubleStats(dbPlayer, playerDto);
			break;
		default:
			break;
		}
		return playerDto;
	}

	private static void setPlayerSingleStats(Player dbPlayer, final PlayerDto playerDto) {
		final PlayerSingleStats playerSingleStats = PMFactory.getObjectById(PlayerSingleStats.class, dbPlayer.getPlayerSingleStats());

		// Single Match
		final PlayerSingleStatsDto playerSingleStatsDto = new PlayerSingleStatsDto();

		playerSingleStatsDto.setSingleWins(playerSingleStats.getWins());
		playerSingleStatsDto.setSingleLosses(playerSingleStats.getLosses());
		playerSingleStatsDto.setSingleShotGoals(playerSingleStats.getShotGoals());
		playerSingleStatsDto.setSingleGetGoals(playerSingleStats.getGetGoals());
		playerSingleStatsDto.setSinglePrevTablePlace(playerSingleStats.getPrevTablePlace());
		playerSingleStatsDto.setSingleCurTablePlace(playerSingleStats.getCurTablePlace());
		playerSingleStatsDto.setSingleLastMatchPoints(playerSingleStats.getLastMatchPoints());
		playerSingleStatsDto.setSinglePoints(playerSingleStats.getPoints());
		playerSingleStatsDto.setSingleTendency(playerSingleStats.getTendency());

		playerDto.setPlayerSingleStats(playerSingleStatsDto);
	}

	private static void setPlayerDoubleStats(Player dbPlayer, PlayerDto playerDto) {
		final PlayerDoubleStats playerDoubleStats = PMFactory.getObjectById(PlayerDoubleStats.class, dbPlayer.getPlayerDoubleStats());

		// Double Match
		final PlayerDoubleStatsDto playerDoubleStatsDto = new PlayerDoubleStatsDto();

		playerDoubleStatsDto.setDoubleWins(playerDoubleStats.getWins());
		playerDoubleStatsDto.setDoubleLosses(playerDoubleStats.getLosses());
		playerDoubleStatsDto.setDoubleShotGoals(playerDoubleStats.getShotGoals());
		playerDoubleStatsDto.setDoubleGetGoals(playerDoubleStats.getGetGoals());
		playerDoubleStatsDto.setDoublePrevTablePlace(playerDoubleStats.getPrevTablePlace());
		playerDoubleStatsDto.setDoubleCurTablePlace(playerDoubleStats.getCurTablePlace());
		playerDoubleStatsDto.setDoubleLastMatchPoints(playerDoubleStats.getLastMatchPoints());
		playerDoubleStatsDto.setDoublePoints(playerDoubleStats.getPoints());
		playerDoubleStatsDto.setDoubleTendency(playerDoubleStats.getTendency());

		playerDto.setPlayerDoubleStats(playerDoubleStatsDto);
	}

}
