package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.MatchService;
import de.kickerapp.server.entity.Match;
import de.kickerapp.server.entity.Match.MatchSets;
import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.PlayerDoubleStats;
import de.kickerapp.server.entity.PlayerSingleStats;
import de.kickerapp.server.entity.Team;
import de.kickerapp.server.entity.TeamStats;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.services.MatchServiceHelper.MatchComparator;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.MatchDto;

/**
 * Dienst zur Verarbeitung von Spielen im Clienten.
 * 
 * @author Sebastian Filke
 */
public class MatchServiceImpl extends RemoteServiceServlet implements MatchService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 6552395856577483840L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MatchDto createMatch(MatchDto matchDto) throws IllegalArgumentException {
		Match match = new Match();

		final int matchId = PMFactory.getNextId(Match.class);
		final Key matchKey = KeyFactory.createKey(Match.class.getSimpleName(), matchId);
		match.setKey(matchKey);
		match.setMatchNumber(matchId);
		match.setMatchDate(matchDto.getMatchDate());

		final MatchSets sets = new MatchSets(matchDto.getSets().getSetsTeam1(), matchDto.getSets().getSetsTeam2());
		match.setMatchSets(sets);

		if (matchDto.getMatchType() == MatchType.Single) {
			createSingleMatch(matchDto, match);
		} else {
			createDoubleMatch(matchDto, match);
		}
		match = PMFactory.persistObject(match);

		updateTable(matchDto);

		return matchDto;
	}

	private void createSingleMatch(MatchDto matchDto, final Match match) {
		match.setMatchType(MatchType.Single);

		Player team1Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam1().getPlayer1().getId());
		Player team2Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam2().getPlayer1().getId());

		final boolean team1Winner = isTeam1Winner(matchDto);
		if (team1Winner) {
			team1Player1 = updatePlayerStats(team1Player1, matchDto, team1Winner);
			team2Player1 = updatePlayerStats(team2Player1, matchDto, !team1Winner);
		} else {
			team1Player1 = updatePlayerStats(team1Player1, matchDto, team1Winner);
			team2Player1 = updatePlayerStats(team2Player1, matchDto, !team1Winner);
		}
		match.setTeam1(team1Player1.getKey().getId());
		match.setTeam2(team2Player1.getKey().getId());
	}

	private void createDoubleMatch(MatchDto matchDto, final Match dbMatch) {
		dbMatch.setMatchType(MatchType.Double);

		Player team1Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam1().getPlayer1().getId());
		Player team1Player2 = PMFactory.getObjectById(Player.class, matchDto.getTeam1().getPlayer2().getId());

		Team team1 = getTeam(team1Player1, team1Player2);
		matchDto.getTeam1().setId(team1.getKey().getId());

		Player team2Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam2().getPlayer1().getId());
		Player team2Player2 = PMFactory.getObjectById(Player.class, matchDto.getTeam2().getPlayer2().getId());

		Team team2 = getTeam(team2Player1, team2Player2);
		matchDto.getTeam2().setId(team2.getKey().getId());

		final boolean team1Winner = isTeam1Winner(matchDto);
		if (team1Winner) {
			team1Player1 = updatePlayerStats(team1Player1, matchDto, team1Winner);
			team1Player2 = updatePlayerStats(team1Player2, matchDto, team1Winner);
			team2Player1 = updatePlayerStats(team2Player1, matchDto, !team1Winner);
			team2Player2 = updatePlayerStats(team2Player2, matchDto, !team1Winner);

			team1 = updateTeamStats(team1, matchDto, team1Winner);
			team2 = updateTeamStats(team2, matchDto, !team1Winner);
		} else {
			team1Player1 = updatePlayerStats(team1Player1, matchDto, team1Winner);
			team1Player2 = updatePlayerStats(team1Player2, matchDto, team1Winner);
			team2Player1 = updatePlayerStats(team2Player1, matchDto, !team1Winner);
			team2Player2 = updatePlayerStats(team2Player2, matchDto, !team1Winner);

			team1 = updateTeamStats(team1, matchDto, team1Winner);
			team2 = updateTeamStats(team2, matchDto, !team1Winner);
		}
		dbMatch.setTeam1(team1.getKey().getId());
		dbMatch.setTeam2(team2.getKey().getId());
	}

	private Team getTeam(Player dbPlayer1, Player dbPlayer2) {
		final Set<Long> existingTeam = new HashSet<Long>();
		existingTeam.addAll(dbPlayer1.getTeams());
		existingTeam.retainAll(dbPlayer2.getTeams());

		Team team = null;
		if (existingTeam.isEmpty()) {
			team = new Team(dbPlayer1, dbPlayer2);
			final int teamId = PMFactory.getNextId(Team.class);
			final Key teamKey = KeyFactory.createKey(Team.class.getSimpleName(), teamId);
			team.setKey(teamKey);

			TeamStats newTeamStats = new TeamStats();
			final int teamStatsId = PMFactory.getNextId(TeamStats.class);
			final Key teamStatsKey = KeyFactory.createKey(TeamStats.class.getSimpleName(), teamStatsId);
			newTeamStats.setKey(teamStatsKey);

			newTeamStats = PMFactory.persistObject(newTeamStats);

			team.setTeamStats(newTeamStats.getKey().getId());

			team = PMFactory.persistObject(team);

			dbPlayer1.getTeams().add(team.getKey().getId());
			dbPlayer2.getTeams().add(team.getKey().getId());
		} else {
			team = PMFactory.getObjectById(Team.class, (Long) existingTeam.toArray()[0]);
		}
		return team;
	}

	private boolean isTeam1Winner(MatchDto matchDto) {
		boolean team1Winner = false;
		int size = 0;
		for (Integer result : matchDto.getSets().getSetsTeam1()) {
			if (result != null && result == 6) {
				size++;
			}
			if (size == 2) {
				team1Winner = true;
				break;
			}
		}
		return team1Winner;
	}

	private Player updatePlayerStats(Player dbPlayer, MatchDto matchDto, boolean winner) {
		if (matchDto.getMatchType() == MatchType.Single) {
			dbPlayer = updatePlayerStatsForSingleMatch(dbPlayer, matchDto, winner);
		} else {
			dbPlayer = updatePlayerStatsForDoubleMatch(dbPlayer, matchDto, winner);
		}
		return dbPlayer;
	}

	private Player updatePlayerStatsForSingleMatch(Player dbPlayer, MatchDto matchDto, boolean winner) {
		final long playerId = dbPlayer.getKey().getId();

		PlayerSingleStats playerSingleStats = PMFactory.getObjectById(PlayerSingleStats.class, dbPlayer.getPlayerSingleStats());

		final int matchPoints = MatchServiceHelper.getPointsForPlayer(winner, dbPlayer, playerSingleStats, matchDto);
		if (winner) {
			final int wins = playerSingleStats.getWins() + 1;
			playerSingleStats.setWins(wins);

			final int points = playerSingleStats.getPoints() + matchPoints;
			playerSingleStats.setPoints(points);
		} else {
			final int losses = playerSingleStats.getLosses() + 1;
			playerSingleStats.setLosses(losses);

			final int points = playerSingleStats.getPoints() + matchPoints;
			playerSingleStats.setPoints(points);
		}
		final int goalsTeam1 = getGoalsTeam1(matchDto);
		final int goalsTeam2 = getGoalsTeam2(matchDto);
		if (matchDto.getTeam1().getPlayer1().getId() == playerId || matchDto.getTeam1().getPlayer2() != null
				&& matchDto.getTeam1().getPlayer2().getId() == playerId) {
			final int shotGoals = playerSingleStats.getShotGoals() + goalsTeam1;
			playerSingleStats.setShotGoals(shotGoals);

			final int getGoals = playerSingleStats.getGetGoals() + goalsTeam2;
			playerSingleStats.setGetGoals(getGoals);
		} else if (matchDto.getTeam2().getPlayer1().getId() == playerId || matchDto.getTeam2().getPlayer2() != null
				&& matchDto.getTeam2().getPlayer2().getId() == playerId) {
			final int shotGoals = playerSingleStats.getShotGoals() + goalsTeam2;
			playerSingleStats.setShotGoals(shotGoals);

			final int getGoals = playerSingleStats.getGetGoals() + goalsTeam1;
			playerSingleStats.setGetGoals(getGoals);
		}
		playerSingleStats.setLastMatchPoints(matchPoints);
		if (dbPlayer.getLastMatchDate() == null || matchDto.getMatchDate().after(dbPlayer.getLastMatchDate())) {
			dbPlayer.setLastMatchDate(matchDto.getMatchDate());
		}

		playerSingleStats = PMFactory.persistObject(playerSingleStats);
		dbPlayer = PMFactory.persistObject(dbPlayer);
		return dbPlayer;
	}

	private Player updatePlayerStatsForDoubleMatch(Player dbPlayer, MatchDto matchDto, boolean winner) {
		final long playerId = dbPlayer.getKey().getId();

		PlayerDoubleStats playerDoubleStats = PMFactory.getObjectById(PlayerDoubleStats.class, dbPlayer.getPlayerDoubleStats());

		final int matchPoints = MatchServiceHelper.getPointsForPlayer(winner, dbPlayer, playerDoubleStats, matchDto);
		if (winner) {
			final int wins = playerDoubleStats.getWins() + 1;
			playerDoubleStats.setWins(wins);

			final int points = playerDoubleStats.getPoints() + matchPoints;
			playerDoubleStats.setPoints(points);
		} else {
			final int losses = playerDoubleStats.getLosses() + 1;
			playerDoubleStats.setLosses(losses);

			final int points = playerDoubleStats.getPoints() + matchPoints;
			;
			playerDoubleStats.setPoints(points);
		}
		final int goalsTeam1 = getGoalsTeam1(matchDto);
		final int goalsTeam2 = getGoalsTeam2(matchDto);
		if (matchDto.getTeam1().getPlayer1().getId() == playerId || matchDto.getTeam1().getPlayer2() != null
				&& matchDto.getTeam1().getPlayer2().getId() == playerId) {
			final int shotGoals = playerDoubleStats.getShotGoals() + goalsTeam1;
			playerDoubleStats.setShotGoals(shotGoals);

			final int getGoals = playerDoubleStats.getGetGoals() + goalsTeam2;
			playerDoubleStats.setGetGoals(getGoals);
		} else if (matchDto.getTeam2().getPlayer1().getId() == playerId || matchDto.getTeam2().getPlayer2() != null
				&& matchDto.getTeam2().getPlayer2().getId() == playerId) {
			final int shotGoals = playerDoubleStats.getShotGoals() + goalsTeam2;
			playerDoubleStats.setShotGoals(shotGoals);

			final int getGoals = playerDoubleStats.getGetGoals() + goalsTeam1;
			playerDoubleStats.setGetGoals(getGoals);
		}
		playerDoubleStats.setLastMatchPoints(matchPoints);
		if (dbPlayer.getLastMatchDate() == null || matchDto.getMatchDate().after(dbPlayer.getLastMatchDate())) {
			dbPlayer.setLastMatchDate(matchDto.getMatchDate());
		}

		playerDoubleStats = PMFactory.persistObject(playerDoubleStats);
		dbPlayer = PMFactory.persistObject(dbPlayer);
		return dbPlayer;
	}

	private Team updateTeamStats(Team dbTeam, MatchDto matchDto, boolean winner) {
		TeamStats teamStats = PMFactory.getObjectById(TeamStats.class, dbTeam.getTeamStats());

		final int matchPoints = MatchServiceHelper.getPointsForTeam(winner, dbTeam, teamStats, matchDto);
		if (winner) {
			final int wins = teamStats.getWins() + 1;
			teamStats.setWins(wins);

			final int points = teamStats.getPoints() + matchPoints;
			teamStats.setPoints(points);
		} else {
			final int losses = teamStats.getLosses() + 1;
			teamStats.setLosses(losses);

			final int points = teamStats.getPoints() + matchPoints;
			teamStats.setPoints(points);
		}
		final int goalsTeam1 = getGoalsTeam1(matchDto);
		final int goalsTeam2 = getGoalsTeam2(matchDto);
		if (dbTeam.getPlayers().contains(matchDto.getTeam1().getPlayer1().getId()) || dbTeam.getPlayers().contains(matchDto.getTeam1().getPlayer2().getId())) {
			final int shotGoals = teamStats.getShotGoals() + goalsTeam1;
			teamStats.setShotGoals(shotGoals);

			final int getGoals = teamStats.getGetGoals() + goalsTeam2;
			teamStats.setGetGoals(getGoals);
		} else if (dbTeam.getPlayers().contains(matchDto.getTeam2().getPlayer1().getId())
				|| dbTeam.getPlayers().contains(matchDto.getTeam2().getPlayer2().getId())) {
			final int shotGoals = teamStats.getShotGoals() + goalsTeam2;
			teamStats.setShotGoals(shotGoals);

			final int getGoals = teamStats.getGetGoals() + goalsTeam1;
			teamStats.setGetGoals(getGoals);
		}
		teamStats.setLastMatchPoints(matchPoints);
		if (dbTeam.getLastMatchDate() == null || matchDto.getMatchDate().after(dbTeam.getLastMatchDate())) {
			dbTeam.setLastMatchDate(matchDto.getMatchDate());
		}

		teamStats = PMFactory.persistObject(teamStats);
		dbTeam = PMFactory.persistObject(dbTeam);
		return dbTeam;
	}

	private int getGoalsTeam1(MatchDto match) {
		int goals = 0;
		for (Integer result : match.getSets().getSetsTeam1()) {
			if (result != null) {
				goals = goals + result;
			}
		}
		return goals;
	}

	private int getGoalsTeam2(MatchDto match) {
		int goals = 0;
		for (Integer result : match.getSets().getSetsTeam2()) {
			if (result != null) {
				goals = goals + result;
			}
		}
		return goals;
	}

	private void updateTable(MatchDto matchDto) {
		if (matchDto.getMatchType() == MatchType.Single) {
			MatchServiceHelper.updateSingleStats();
		} else {
			MatchServiceHelper.updateDoubleStats();
			MatchServiceHelper.updateTeamStats();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<MatchDto> getAllMatches() throws IllegalArgumentException {
		final ArrayList<MatchDto> matches = new ArrayList<MatchDto>();

		final List<Match> dbMatches = PMFactory.getList(Match.class);

		Collections.sort(dbMatches, new MatchComparator());
		for (Match dbMatch : dbMatches) {
			final MatchDto matchDto = MatchServiceHelper.createMatch(dbMatch);

			matches.add(matchDto);
		}
		return matches;
	}

}
