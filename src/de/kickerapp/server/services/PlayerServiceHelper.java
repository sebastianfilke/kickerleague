package de.kickerapp.server.services;

import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.Player.PlayerStats;
import de.kickerapp.shared.dto.PlayerDto;

public class PlayerServiceHelper {

	public static PlayerDto createPlayer(Player dbPlayer) {
		final PlayerDto player = new PlayerDto(dbPlayer.getLastName(), dbPlayer.getFirstName());
		player.setId(dbPlayer.getKey().getId());
		player.setNickName(dbPlayer.getNickName());
		player.setEMail(dbPlayer.getEMail());

		final PlayerStats playerStats = dbPlayer.getPlayerStats();

		// Single Match
		final int wins = playerStats.getSingleWins();
		final int losses = playerStats.getSingleLosses();

		player.setSingleMatches(wins + losses);
		player.setSingleWins(wins);
		player.setSingleLosses(losses);
		player.setSingleGoals(playerStats.getSingleShotGoals() + ":" + playerStats.getSingleGetGoals());

		final int goalDifference = playerStats.getSingleShotGoals() - playerStats.getSingleGetGoals();
		if (goalDifference >= 0) {
			player.setSingleGoalDifference("+" + Integer.toString(goalDifference));
		} else {
			player.setSingleGoalDifference(Integer.toString(goalDifference));
		}

		// Double Match
		player.setDoubleWins(playerStats.getDoubleWins());
		player.setDoubleLosses(playerStats.getDoubleLosses());
		player.setDoubleGoals(playerStats.getDoubleShotGoals() + ":" + playerStats.getDoubleGetGoals());
		player.setPoints(playerStats.getPoints());
		player.setTendency(playerStats.getTendency());

		return player;
	}

}
