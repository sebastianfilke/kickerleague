package de.kickerapp.server.services;

import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.Team;
import de.kickerapp.server.entity.Team.TeamStats;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;

public class TeamServiceHelper {

	public static TeamDto createTeam(Team dbTeam) {
		final Player dbPlayer1 = PMFactory.getObjectById(Player.class, (Long) dbTeam.getPlayers().toArray()[0]);
		final Player dbPlayer2 = PMFactory.getObjectById(Player.class, (Long) dbTeam.getPlayers().toArray()[1]);

		final PlayerDto player1 = PlayerServiceHelper.createPlayer(dbPlayer1);
		final PlayerDto player2 = PlayerServiceHelper.createPlayer(dbPlayer2);

		final TeamDto teamDto = new TeamDto(player1, player2);
		teamDto.setId(dbTeam.getKey().getId());

		final TeamStats teamStats = dbTeam.getTeamStats();

		teamDto.setWins(teamStats.getWins());
		teamDto.setLosses(teamStats.getLosses());
		teamDto.setShotGoals(teamStats.getShotGoals());
		teamDto.setGetGoals(teamStats.getGetGoals());
		teamDto.setPoints(teamStats.getPoints());
		teamDto.setTendency(teamStats.getTendency());

		return teamDto;
	}

}
