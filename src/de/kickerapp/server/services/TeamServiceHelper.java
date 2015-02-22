package de.kickerapp.server.services;

import java.util.Comparator;

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
 * Hilfsklasse für den Dienst zur Verarbeitung von Teams im Klienten.
 * 
 * @author Sebastian Filke
 */
public class TeamServiceHelper {

	/**
	 * Comparator zur Sortierung der Teams für die Tabelle.
	 * 
	 * @author Sebastian Filke
	 */
	protected static class TeamTableComparator implements Comparator<TeamDto> {

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
	 * @return Die Client-Datenklasse ohne Spielstatistiken.
	 */
	protected static TeamDto createTeamDto(Team dbTeam) {
		final PlayerDto playerDto1 = PlayerServiceHelper.createPlayerDto(dbTeam.getPlayer1());
		final PlayerDto playerDto2 = PlayerServiceHelper.createPlayerDto(dbTeam.getPlayer2());

		final TeamDto teamDto = new TeamDto(playerDto1, playerDto2);
		teamDto.setId(dbTeam.getKey().getId());
		teamDto.setLastMatchDate(dbTeam.getLastMatchDate());

		return teamDto;
	}

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbTeam Die Objekt-Datenklasse.
	 * @return Die Client-Datenklasse mit Teamspiel-Statistik.
	 */
	protected static TeamDto createTeamDtoWithTeamStats(Team dbTeam) {
		final TeamDto teamDto = createTeamDto(dbTeam);
		teamDto.setTeamStatsDto(createTeamStats(dbTeam));

		return teamDto;
	}

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbTeam Die Objekt-Datenklasse.
	 * @return Die Client-Datenklasse.
	 */
	private static TeamStatsDto createTeamStats(Team dbTeam) {
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

		return teamStatsDto;
	}

	/**
	 * Liefert oder erzeugt, falls noch nicht vorhanden, ein Team.
	 * 
	 * @param dbPlayer1 Der erste Spieler des Teams.
	 * @param dbPlayer2 Der zweite Spieler des Teams.
	 * @return Das Team.
	 */
	protected static Team getTeam(Player dbPlayer1, Player dbPlayer2) {
		final Team existingTeam = determineTeamForPlayers(dbPlayer1, dbPlayer2);

		Team dbTeam = null;
		if (existingTeam == null) {
			dbTeam = createTeam(dbPlayer1, dbPlayer2);
		} else {
			dbTeam = PMFactory.getObjectById(Team.class, (Long) existingTeam.getKey().getId(), TeamPlan.TEAMSTATS, TeamPlan.BOTHPLAYERS,
					PlayerPlan.PLAYERDOUBLESTATS);
		}
		return dbTeam;
	}

	/**
	 * Bestimmt das gemeinsame Team der Spieler falls eines existiert.
	 * 
	 * @param dbPlayer1 Der erste Spieler des Teams.
	 * @param dbPlayer2 Der zweite Spieler des Teams.
	 * @return Das Team der Spieler oder <code>null</code>.
	 */
	private static Team determineTeamForPlayers(Player dbPlayer1, Player dbPlayer2) {
		for (Team teamPlayer1 : dbPlayer1.getTeams()) {
			for (Team teamPlayer2 : dbPlayer2.getTeams()) {
				if (teamPlayer1.getKey().getId() == teamPlayer2.getKey().getId()) {
					return teamPlayer1;
				}
			}
		}
		return null;
	}

	/**
	 * Erzeugt ein neues Team.
	 * 
	 * @param dbPlayer1 Der erste Spieler des Teams.
	 * @param dbPlayer2 Der zweite Spieler des Teams.
	 * @return Das neu erstellte Team.
	 */
	private static Team createTeam(Player dbPlayer1, Player dbPlayer2) {
		Team dbTeam = new Team(dbPlayer1, dbPlayer2);
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

		return dbTeam;
	}

}
