package de.kickerapp.server.services;

import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.Player.PlayerStats;
import de.kickerapp.shared.dto.PlayerDto;

public class PlayerServiceHelper {

	public static PlayerDto createPlayer(Player dbPlayer) {
		final PlayerDto playerDto = new PlayerDto(dbPlayer.getLastName(), dbPlayer.getFirstName());
		playerDto.setId(dbPlayer.getKey().getId());
		playerDto.setNickName(dbPlayer.getNickName());
		playerDto.setEMail(dbPlayer.getEMail());
		playerDto.setLastMatchDate(dbPlayer.getLastMatchDate());

		final PlayerStats playerStats = dbPlayer.getPlayerStats();

		// Single Match
		final int wins = playerStats.getSingleWins();
		final int losses = playerStats.getSingleLosses();

		playerDto.setSingleMatches(wins + losses);
		playerDto.setSingleWins(wins);
		playerDto.setSingleLosses(losses);
		playerDto.setSingleGoals(playerStats.getSingleShotGoals() + ":" + playerStats.getSingleGetGoals());

		final int goalDifference = playerStats.getSingleShotGoals() - playerStats.getSingleGetGoals();
		if (goalDifference >= 0) {
			playerDto.setSingleGoalDifference("+" + Integer.toString(goalDifference));
		} else {
			playerDto.setSingleGoalDifference(Integer.toString(goalDifference));
		}

		// Double Match
		playerDto.setDoubleWins(playerStats.getDoubleWins());
		playerDto.setDoubleLosses(playerStats.getDoubleLosses());
		playerDto.setDoubleGoals(playerStats.getDoubleShotGoals() + ":" + playerStats.getDoubleGetGoals());
		playerDto.setPoints(playerStats.getPoints());
		playerDto.setTendency(playerStats.getTendency());

		return playerDto;
	}

}
