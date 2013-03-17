package de.kickerapp.server.services;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.Team;
import de.kickerapp.server.entity.TeamStats;
import de.kickerapp.server.persistence.PMFactory;
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
	 * @param dbTeamStats Die Objekt-Datenklasse-Liste aller Teamspiel-Statistiken.
	 * @param dbPlayers Die Objekt-Datenklasse-Liste aller Spieler.
	 * @return Die Client-Datenklasse.
	 */
	protected static TeamDto createDtoTeam(Team dbTeam, List<TeamStats> dbTeamStats, List<Player> dbPlayers) {
		final Player dbPlayer1 = PlayerServiceHelper.getPlayerById((Long) dbTeam.getPlayers().toArray()[0], dbPlayers);
		final Player dbPlayer2 = PlayerServiceHelper.getPlayerById((Long) dbTeam.getPlayers().toArray()[1], dbPlayers);

		final PlayerDto playerDto1 = PlayerServiceHelper.createPlayerDto(dbPlayer1);
		final PlayerDto playerDto2 = PlayerServiceHelper.createPlayerDto(dbPlayer2);

		final TeamDto teamDto = new TeamDto(playerDto1, playerDto2);
		teamDto.setId(dbTeam.getKey().getId());
		teamDto.setLastMatchDate(dbTeam.getLastMatchDate());

		final TeamStats dbTeamStat = getTeamStatById(dbTeam.getTeamStats(), dbTeamStats);

		final TeamStatsDto teamStatsDto = new TeamStatsDto();

		teamStatsDto.setWins(dbTeamStat.getWins());
		teamStatsDto.setLosses(dbTeamStat.getLosses());
		teamStatsDto.setShotGoals(dbTeamStat.getShotGoals());
		teamStatsDto.setGetGoals(dbTeamStat.getGetGoals());
		teamStatsDto.setPrevTablePlace(dbTeamStat.getPrevTablePlace());
		teamStatsDto.setCurTablePlace(dbTeamStat.getCurTablePlace());
		teamStatsDto.setLastMatchPoints(dbTeamStat.getLastMatchPoints());
		teamStatsDto.setPoints(dbTeamStat.getPoints());
		teamStatsDto.setTendency(dbTeamStat.getTendency());

		teamDto.setTeamStatsDto(teamStatsDto);

		return teamDto;
	}

	/**
	 * Liefert das Team anhand der Db-Id.
	 * 
	 * @param id Die Db-Id des Teams.
	 * @param dbTeams Die Objekt-Datenklasse-Liste aller Teams.
	 * @return Das Team.
	 */
	protected static Team getTeamById(Long id, List<Team> dbTeams) {
		Team dbTeam = null;
		for (Team dbCurrentTeam : dbTeams) {
			if (dbCurrentTeam.getKey().getId() == id) {
				dbTeam = dbCurrentTeam;
				break;
			}
		}
		return dbTeam;
	}

	/**
	 * Liefert das Teamspiel-Statistik anhand der Db-Id.
	 * 
	 * @param id Die Db-Id der Teamspiel-Statistik.
	 * @param dbTeamStats Die Objekt-Datenklasse-Liste aller Teamspiel-Statistiken.
	 * @return Die Teamspiel-Statistik.
	 */
	protected static TeamStats getTeamStatById(Long id, List<TeamStats> dbTeamStats) {
		TeamStats dbTeamStat = null;
		for (TeamStats dbCurrentTeamStats : dbTeamStats) {
			if (dbCurrentTeamStats.getKey().getId() == id) {
				dbTeamStat = dbCurrentTeamStats;
				break;
			}
		}
		return dbTeamStat;
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
