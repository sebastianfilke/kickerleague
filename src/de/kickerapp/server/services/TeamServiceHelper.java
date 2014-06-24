package de.kickerapp.server.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.Team;
import de.kickerapp.server.dao.TeamStats;
import de.kickerapp.server.dao.fetchplans.PlayerPlan;
import de.kickerapp.server.dao.fetchplans.TeamPlan;
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
	 * @return Die Client-Datenklasse.
	 */
	protected static TeamDto createDtoTeam(Team dbTeam) {
		final PlayerDto playerDto1 = PlayerServiceHelper.createPlayerDto(dbTeam.getPlayer1());
		final PlayerDto playerDto2 = PlayerServiceHelper.createPlayerDto(dbTeam.getPlayer2());

		final TeamDto teamDto = new TeamDto(playerDto1, playerDto2);
		teamDto.setId(dbTeam.getKey().getId());
		teamDto.setLastMatchDate(dbTeam.getLastMatchDate());

		final TeamStats dbTeamStat = dbTeam.getTeamStats();

		final TeamStatsDto teamStatsDto = new TeamStatsDto();
		teamStatsDto.setId(dbTeamStat.getKey().getId());
		teamStatsDto.setWins(dbTeamStat.getWins());
		teamStatsDto.setDefeats(dbTeamStat.getDefeats());
		teamStatsDto.setWinSets(dbTeamStat.getWinSets());
		teamStatsDto.setLostSets(dbTeamStat.getLostSets());
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
		final TreeSet<Long> existingTeam = new TreeSet<Long>();

		final List<Long> team1Ids = new ArrayList<Long>();
		final List<Long> team2Ids = new ArrayList<Long>();
		for (Team team : dbPlayer1.getTeams()) {
			team1Ids.add(team.getKey().getId());
		}
		for (Team team : dbPlayer2.getTeams()) {
			team2Ids.add(team.getKey().getId());
		}
		existingTeam.addAll(team1Ids);
		existingTeam.retainAll(team2Ids);

		Team dbTeam = null;
		if (existingTeam.isEmpty()) {
			dbTeam = new Team(dbPlayer1, dbPlayer2);
			final int teamId = PMFactory.getNextId(Team.class.getName());
			final Key teamKey = KeyFactory.createKey(Team.class.getSimpleName(), teamId);
			dbTeam.setKey(teamKey);

			final TeamStats dbTeamStats = new TeamStats();
			final int teamStatsId = PMFactory.getNextId(TeamStats.class.getName());
			final Key teamStatsKey = KeyFactory.createKey(dbTeam.getKey(), TeamStats.class.getSimpleName(), teamStatsId);
			dbTeamStats.setKey(teamStatsKey);

			dbTeam.setTeamStats(dbTeamStats);
			dbPlayer1.getTeams().add(dbTeam);
			dbPlayer2.getTeams().add(dbTeam);

			dbTeam = PMFactory.persistObject(dbTeam);
		} else {
			dbTeam = PMFactory.getObjectById(Team.class, (Long) existingTeam.toArray()[0], TeamPlan.TEAMSTATS, TeamPlan.BOTHPLAYERS,
					PlayerPlan.PLAYERDOUBLESTATS);
		}
		return dbTeam;
	}

}
