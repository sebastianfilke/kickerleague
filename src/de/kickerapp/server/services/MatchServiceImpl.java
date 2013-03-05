package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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
		Match dbMatch = new Match();

		final int matchId = PMFactory.getNextId(Match.class);
		final Key matchKey = KeyFactory.createKey(Match.class.getSimpleName(), matchId);
		dbMatch.setKey(matchKey);
		dbMatch.setMatchNumber(matchId);
		dbMatch.setMatchDate(matchDto.getMatchDate());

		final MatchSets sets = new MatchSets(matchDto.getSets().getSetsTeam1(), matchDto.getSets().getSetsTeam2());
		dbMatch.setMatchSets(sets);

		if (matchDto.getMatchType() == MatchType.SINGLE) {
			createSingleMatch(matchDto, dbMatch);
		} else {
			createDoubleMatch(matchDto, dbMatch);
		}
		dbMatch = PMFactory.persistObject(dbMatch);

		updateTable(matchDto);

		return matchDto;
	}

	private void createSingleMatch(final MatchDto matchDto, final Match dbMatch) {
		dbMatch.setMatchType(MatchType.SINGLE);

		final Player team1Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam1().getPlayer1().getId());
		final Player team2Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam2().getPlayer1().getId());

		dbMatch.setTeam1(team1Player1.getKey().getId());
		dbMatch.setTeam2(team2Player1.getKey().getId());

		final boolean team1Winner = isTeam1Winner(matchDto);
		updatePlayerStats(team1Player1, matchDto, dbMatch, team1Winner);
		updatePlayerStats(team2Player1, matchDto, dbMatch, !team1Winner);
	}

	private void createDoubleMatch(final MatchDto matchDto, final Match dbMatch) {
		dbMatch.setMatchType(MatchType.DOUBLE);

		final Player dbTeam1Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam1().getPlayer1().getId());
		final Player dbTeam1Player2 = PMFactory.getObjectById(Player.class, matchDto.getTeam1().getPlayer2().getId());

		final Team dbTeam1 = getTeam(dbTeam1Player1, dbTeam1Player2);
		matchDto.getTeam1().setId(dbTeam1.getKey().getId());

		final Player dbTeam1Player1Sorted = getPlayerForTeamId(dbTeam1Player1, dbTeam1Player2, (Long) dbTeam1.getPlayers().toArray()[0]);
		final Player dbTeam1Player2Sorted = getPlayerForTeamId(dbTeam1Player1, dbTeam1Player2, (Long) dbTeam1.getPlayers().toArray()[1]);

		final Player dbTeam2Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam2().getPlayer1().getId());
		final Player dbTeam2Player2 = PMFactory.getObjectById(Player.class, matchDto.getTeam2().getPlayer2().getId());

		final Team dbTeam2 = getTeam(dbTeam2Player1, dbTeam2Player2);
		matchDto.getTeam2().setId(dbTeam2.getKey().getId());

		final Player dbTeam2Player1Sorted = getPlayerForTeamId(dbTeam2Player1, dbTeam2Player2, (Long) dbTeam2.getPlayers().toArray()[0]);
		final Player dbTeam2Player2Sorted = getPlayerForTeamId(dbTeam2Player1, dbTeam2Player2, (Long) dbTeam2.getPlayers().toArray()[1]);

		dbMatch.setTeam1(dbTeam1.getKey().getId());
		dbMatch.setTeam2(dbTeam2.getKey().getId());

		final boolean team1Winner = isTeam1Winner(matchDto);
		updatePlayerStats(dbTeam1Player1Sorted, matchDto, dbMatch, team1Winner);
		updatePlayerStats(dbTeam1Player2Sorted, matchDto, dbMatch, team1Winner);
		updatePlayerStats(dbTeam2Player1Sorted, matchDto, dbMatch, !team1Winner);
		updatePlayerStats(dbTeam2Player2Sorted, matchDto, dbMatch, !team1Winner);

		updateTeamStats(dbTeam1, matchDto, dbMatch, team1Winner);
		updateTeamStats(dbTeam2, matchDto, dbMatch, !team1Winner);
	}

	private Player getPlayerForTeamId(Player dbTeam1Player1, Player dbTeam1Player2, Long id) {
		Player player = null;
		if (dbTeam1Player1.getKey().getId() == id) {
			player = dbTeam1Player1;
		} else if (dbTeam1Player2.getKey().getId() == id) {
			player = dbTeam1Player2;
		}
		return player;
	}

	private Team getTeam(Player dbPlayer1, Player dbPlayer2) {
		final HashSet<Long> existingTeam = new HashSet<Long>();
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

	private void updatePlayerStats(Player dbPlayer, MatchDto matchDto, Match dbMatch, boolean winner) {
		if (matchDto.getMatchType() == MatchType.SINGLE) {
			updatePlayerStatsForSingleMatch(dbPlayer, matchDto, dbMatch, winner);
		} else {
			updatePlayerStatsForDoubleMatch(dbPlayer, matchDto, dbMatch, winner);
		}
	}

	private void updatePlayerStatsForSingleMatch(Player dbPlayer, MatchDto matchDto, Match dbMatch, boolean winner) {
		final long playerId = dbPlayer.getKey().getId();

		final PlayerSingleStats playerSingleStats = PMFactory.getObjectById(PlayerSingleStats.class, dbPlayer.getPlayerSingleStats());

		final int matchPoints = MatchServiceHelper.getPointsForPlayer(winner, dbPlayer, playerSingleStats, matchDto);
		if (winner) {
			final int wins = playerSingleStats.getWins() + 1;
			playerSingleStats.setWins(wins);
		} else {
			final int losses = playerSingleStats.getLosses() + 1;
			playerSingleStats.setLosses(losses);
		}
		final int points = playerSingleStats.getPoints() + matchPoints;
		playerSingleStats.setPoints(points);

		final int goalsTeam1 = MatchServiceHelper.getGoalsTeam1(matchDto);
		final int goalsTeam2 = MatchServiceHelper.getGoalsTeam2(matchDto);
		if (matchDto.getTeam1().getPlayer1().getId() == playerId || matchDto.getTeam1().getPlayer2() != null
				&& matchDto.getTeam1().getPlayer2().getId() == playerId) {
			dbMatch.getMatchPoints().getMatchPointsTeam1().add(matchPoints);

			final int shotGoals = playerSingleStats.getShotGoals() + goalsTeam1;
			playerSingleStats.setShotGoals(shotGoals);

			final int getGoals = playerSingleStats.getGetGoals() + goalsTeam2;
			playerSingleStats.setGetGoals(getGoals);
		} else if (matchDto.getTeam2().getPlayer1().getId() == playerId || matchDto.getTeam2().getPlayer2() != null
				&& matchDto.getTeam2().getPlayer2().getId() == playerId) {
			dbMatch.getMatchPoints().getMatchPointsTeam2().add(matchPoints);

			final int shotGoals = playerSingleStats.getShotGoals() + goalsTeam2;
			playerSingleStats.setShotGoals(shotGoals);

			final int getGoals = playerSingleStats.getGetGoals() + goalsTeam1;
			playerSingleStats.setGetGoals(getGoals);
		}
		playerSingleStats.setLastMatchPoints(matchPoints);
		if (dbPlayer.getLastMatchDate() == null || matchDto.getMatchDate().after(dbPlayer.getLastMatchDate())) {
			dbPlayer.setLastMatchDate(matchDto.getMatchDate());
		}

		PMFactory.persistObject(playerSingleStats);
		PMFactory.persistObject(dbPlayer);
	}

	private void updatePlayerStatsForDoubleMatch(Player dbPlayer, MatchDto matchDto, Match dbMatch, boolean winner) {
		final long playerId = dbPlayer.getKey().getId();

		final PlayerDoubleStats playerDoubleStats = PMFactory.getObjectById(PlayerDoubleStats.class, dbPlayer.getPlayerDoubleStats());

		final int matchPoints = MatchServiceHelper.getPointsForPlayer(winner, dbPlayer, playerDoubleStats, matchDto);
		if (winner) {
			final int wins = playerDoubleStats.getWins() + 1;
			playerDoubleStats.setWins(wins);
		} else {
			final int losses = playerDoubleStats.getLosses() + 1;
			playerDoubleStats.setLosses(losses);
		}
		final int points = playerDoubleStats.getPoints() + matchPoints;
		playerDoubleStats.setPoints(points);

		final int goalsTeam1 = MatchServiceHelper.getGoalsTeam1(matchDto);
		final int goalsTeam2 = MatchServiceHelper.getGoalsTeam2(matchDto);
		if (matchDto.getTeam1().getPlayer1().getId() == playerId || matchDto.getTeam1().getPlayer2() != null
				&& matchDto.getTeam1().getPlayer2().getId() == playerId) {
			dbMatch.getMatchPoints().getMatchPointsTeam1().add(matchPoints);

			final int shotGoals = playerDoubleStats.getShotGoals() + goalsTeam1;
			playerDoubleStats.setShotGoals(shotGoals);

			final int getGoals = playerDoubleStats.getGetGoals() + goalsTeam2;
			playerDoubleStats.setGetGoals(getGoals);
		} else if (matchDto.getTeam2().getPlayer1().getId() == playerId || matchDto.getTeam2().getPlayer2() != null
				&& matchDto.getTeam2().getPlayer2().getId() == playerId) {
			dbMatch.getMatchPoints().getMatchPointsTeam2().add(matchPoints);

			final int shotGoals = playerDoubleStats.getShotGoals() + goalsTeam2;
			playerDoubleStats.setShotGoals(shotGoals);

			final int getGoals = playerDoubleStats.getGetGoals() + goalsTeam1;
			playerDoubleStats.setGetGoals(getGoals);
		}
		playerDoubleStats.setLastMatchPoints(matchPoints);
		if (dbPlayer.getLastMatchDate() == null || matchDto.getMatchDate().after(dbPlayer.getLastMatchDate())) {
			dbPlayer.setLastMatchDate(matchDto.getMatchDate());
		}

		PMFactory.persistObject(playerDoubleStats);
		PMFactory.persistObject(dbPlayer);
	}

	private void updateTeamStats(Team dbTeam, MatchDto matchDto, Match dbMatch, boolean winner) {
		final TeamStats teamStats = PMFactory.getObjectById(TeamStats.class, dbTeam.getTeamStats());

		final int matchPoints = MatchServiceHelper.getPointsForTeam(winner, dbTeam, teamStats, matchDto);
		if (winner) {
			final int wins = teamStats.getWins() + 1;
			teamStats.setWins(wins);
		} else {
			final int losses = teamStats.getLosses() + 1;
			teamStats.setLosses(losses);
		}
		final int points = teamStats.getPoints() + matchPoints;
		teamStats.setPoints(points);

		final int goalsTeam1 = MatchServiceHelper.getGoalsTeam1(matchDto);
		final int goalsTeam2 = MatchServiceHelper.getGoalsTeam2(matchDto);
		if (dbTeam.getPlayers().contains(matchDto.getTeam1().getPlayer1().getId()) || dbTeam.getPlayers().contains(matchDto.getTeam1().getPlayer2().getId())) {
			dbMatch.getMatchPoints().getMatchPointsTeam1().add(matchPoints);

			final int shotGoals = teamStats.getShotGoals() + goalsTeam1;
			teamStats.setShotGoals(shotGoals);

			final int getGoals = teamStats.getGetGoals() + goalsTeam2;
			teamStats.setGetGoals(getGoals);
		} else if (dbTeam.getPlayers().contains(matchDto.getTeam2().getPlayer1().getId())
				|| dbTeam.getPlayers().contains(matchDto.getTeam2().getPlayer2().getId())) {
			dbMatch.getMatchPoints().getMatchPointsTeam2().add(matchPoints);

			final int shotGoals = teamStats.getShotGoals() + goalsTeam2;
			teamStats.setShotGoals(shotGoals);

			final int getGoals = teamStats.getGetGoals() + goalsTeam1;
			teamStats.setGetGoals(getGoals);
		}
		teamStats.setLastMatchPoints(matchPoints);
		if (dbTeam.getLastMatchDate() == null || matchDto.getMatchDate().after(dbTeam.getLastMatchDate())) {
			dbTeam.setLastMatchDate(matchDto.getMatchDate());
		}

		PMFactory.persistObject(teamStats);
		PMFactory.persistObject(dbTeam);
	}

	private void updateTable(MatchDto matchDto) {
		if (matchDto.getMatchType() == MatchType.SINGLE) {
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
