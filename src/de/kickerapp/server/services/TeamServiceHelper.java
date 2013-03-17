package de.kickerapp.server.services;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.Team;
import de.kickerapp.server.entity.TeamStats;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;
import de.kickerapp.shared.dto.TeamStatsDto;

/**
 * Hilfsklasse für den Dienst zur Verarbeitung von Teams im Clienten.
 * 
 * @author Sebastian Filke
 */
public class TeamServiceHelper {

	/**
	 * Comparator zur Sortierung der Teams für die Tabelle.
	 * 
	 * @author Sebastian Filke
	 */
	protected static class TeamTableComparator implements Comparator<TeamDto>, Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = -3174540122634747743L;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(TeamDto t1, TeamDto t2) {
			return t1.getTeamStatsDto().getCurTablePlace().compareTo(t2.getTeamStatsDto().getCurTablePlace());
		}
	}

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbTeam Die Objekt-Datenklasse.
	 * @return Die Client-Datenklasse.
	 */
	protected static TeamDto createDtoTeam(Team dbTeam) {
		final Player dbPlayer1 = PMFactory.getObjectById(Player.class, (Long) dbTeam.getPlayers().toArray()[0]);
		final Player dbPlayer2 = PMFactory.getObjectById(Player.class, (Long) dbTeam.getPlayers().toArray()[1]);

		final PlayerDto playerDto1 = PlayerServiceHelper.createDtoPlayer(dbPlayer1, MatchType.NONE);
		final PlayerDto playerDto2 = PlayerServiceHelper.createDtoPlayer(dbPlayer2, MatchType.NONE);

		final TeamDto teamDto = new TeamDto(playerDto1, playerDto2);
		teamDto.setId(dbTeam.getKey().getId());
		teamDto.setLastMatchDate(dbTeam.getLastMatchDate());

		final TeamStats dbTeamStats = PMFactory.getObjectById(TeamStats.class, dbTeam.getTeamStats());

		// Team Match
		final TeamStatsDto teamStatsDto = new TeamStatsDto();

		teamStatsDto.setWins(dbTeamStats.getWins());
		teamStatsDto.setLosses(dbTeamStats.getLosses());
		teamStatsDto.setShotGoals(dbTeamStats.getShotGoals());
		teamStatsDto.setGetGoals(dbTeamStats.getGetGoals());
		teamStatsDto.setPrevTablePlace(dbTeamStats.getPrevTablePlace());
		teamStatsDto.setCurTablePlace(dbTeamStats.getCurTablePlace());
		teamStatsDto.setLastMatchPoints(dbTeamStats.getLastMatchPoints());
		teamStatsDto.setPoints(dbTeamStats.getPoints());
		teamStatsDto.setTendency(dbTeamStats.getTendency());

		teamDto.setTeamStatsDto(teamStatsDto);

		return teamDto;
	}

	/**
	 * Liefert oder erzeugt, falls noch nicht vorhanden, ein Team.
	 * 
	 * @param dbPlayer1 Der erste Spieler des Teams.
	 * @param dbPlayer2 Der zweite Spieler des Teams.
	 * @return Das Team.
	 */
	protected static Team getTeam(Player dbPlayer1, Player dbPlayer2) {
		final HashSet<Long> existingTeam = new HashSet<Long>();
		existingTeam.addAll(dbPlayer1.getTeams());
		existingTeam.retainAll(dbPlayer2.getTeams());

		Team dbTeam = null;
		if (existingTeam.isEmpty()) {
			dbTeam = new Team(dbPlayer1, dbPlayer2);
			final int teamId = PMFactory.getNextId(Team.class.getName());
			final Key teamKey = KeyFactory.createKey(Team.class.getSimpleName(), teamId);
			dbTeam.setKey(teamKey);

			final TeamStats dbTeamStats = new TeamStats();
			final int teamStatsId = PMFactory.getNextId(TeamStats.class.getName());
			final Key teamStatsKey = KeyFactory.createKey(TeamStats.class.getSimpleName(), teamStatsId);
			dbTeamStats.setKey(teamStatsKey);

			dbTeam.setTeamStats(dbTeamStats.getKey().getId());
			dbPlayer1.getTeams().add(dbTeam.getKey().getId());
			dbPlayer2.getTeams().add(dbTeam.getKey().getId());

			final Object[] persistedResult = PMFactory.persistAllObjects(dbTeamStats, dbTeam);
			dbTeam = (Team) persistedResult[1];
		} else {
			dbTeam = PMFactory.getObjectById(Team.class, (Long) existingTeam.toArray()[0]);
		}
		return dbTeam;
	}

}
