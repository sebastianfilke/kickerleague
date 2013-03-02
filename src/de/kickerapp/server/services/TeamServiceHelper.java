package de.kickerapp.server.services;

import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.Team;
import de.kickerapp.server.entity.TeamStats;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;
import de.kickerapp.shared.dto.TeamStatsDto;

public class TeamServiceHelper {

	public static TeamDto createTeam(Team dbTeam) {
		final Player dbPlayer1 = PMFactory.getObjectById(Player.class, (Long) dbTeam.getPlayers().toArray()[0]);
		final Player dbPlayer2 = PMFactory.getObjectById(Player.class, (Long) dbTeam.getPlayers().toArray()[1]);

		final PlayerDto player1 = PlayerServiceHelper.createPlayer(dbPlayer1, MatchType.NONE);
		final PlayerDto player2 = PlayerServiceHelper.createPlayer(dbPlayer2, MatchType.NONE);

		final TeamDto teamDto = new TeamDto(player1, player2);
		teamDto.setId(dbTeam.getKey().getId());
		teamDto.setLastMatchDate(dbTeam.getLastMatchDate());

		final TeamStats teamStats = PMFactory.getObjectById(TeamStats.class, dbTeam.getTeamStats());

		// Team Match
		final TeamStatsDto teamStatsDto = new TeamStatsDto();

		teamStatsDto.setWins(teamStats.getWins());
		teamStatsDto.setLosses(teamStats.getLosses());
		teamStatsDto.setShotGoals(teamStats.getShotGoals());
		teamStatsDto.setGetGoals(teamStats.getGetGoals());
		teamStatsDto.setPrevTablePlace(teamStats.getPrevTablePlace());
		teamStatsDto.setCurTablePlace(teamStats.getCurTablePlace());
		teamStatsDto.setLastMatchPoints(teamStats.getLastMatchPoints());
		teamStatsDto.setPoints(teamStats.getPoints());
		teamStatsDto.setTendency(teamStats.getTendency());

		teamDto.setTeamStatsDto(teamStatsDto);

		return teamDto;
	}

}
