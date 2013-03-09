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
		final Match dbMatch = new Match();

		final int matchId = PMFactory.getNextId(Match.class.getName());
		final Key matchKey = KeyFactory.createKey(Match.class.getSimpleName(), matchId);
		dbMatch.setKey(matchKey);
		dbMatch.setMatchNumber(matchId);
		dbMatch.setMatchDate(matchDto.getMatchDate());

		final MatchSets sets = new MatchSets(matchDto.getMatchSetsDto().getMatchSetsTeam1(), matchDto.getMatchSetsDto().getMatchSetsTeam2());
		dbMatch.setMatchSets(sets);

		if (matchDto.getMatchType() == MatchType.SINGLE) {
			createSingleMatch(matchDto, dbMatch);
		} else {
			createDoubleMatch(matchDto, dbMatch);
		}
		PMFactory.persistObject(dbMatch);

		MatchServiceHelper.updateTable(matchDto);

		return matchDto;
	}

	private void createSingleMatch(final MatchDto matchDto, Match dbMatch) {
		dbMatch.setMatchType(MatchType.SINGLE);

		final Player dbTeam1Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam1Dto().getPlayer1().getId());
		final Player dbTeam2Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam2Dto().getPlayer1().getId());

		dbMatch.setTeam1(dbTeam1Player1.getKey().getId());
		dbMatch.setTeam2(dbTeam2Player1.getKey().getId());

		final boolean team1Winner = isTeam1Winner(matchDto);
		updatePlayerStats(dbTeam1Player1, matchDto, dbMatch, team1Winner);
		updatePlayerStats(dbTeam2Player1, matchDto, dbMatch, !team1Winner);
	}

	private void createDoubleMatch(final MatchDto matchDto, final Match dbMatch) {
		dbMatch.setMatchType(MatchType.DOUBLE);

		final Player dbTeam1Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam1Dto().getPlayer1().getId());
		final Player dbTeam1Player2 = PMFactory.getObjectById(Player.class, matchDto.getTeam1Dto().getPlayer2().getId());

		final Team dbTeam1 = getTeam(dbTeam1Player1, dbTeam1Player2);
		matchDto.getTeam1Dto().setId(dbTeam1.getKey().getId());

		final Player dbTeam1Player1Sorted = getPlayerForTeamId(dbTeam1Player1, dbTeam1Player2, (Long) dbTeam1.getPlayers().toArray()[0]);
		final Player dbTeam1Player2Sorted = getPlayerForTeamId(dbTeam1Player1, dbTeam1Player2, (Long) dbTeam1.getPlayers().toArray()[1]);

		final Player dbTeam2Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam2Dto().getPlayer1().getId());
		final Player dbTeam2Player2 = PMFactory.getObjectById(Player.class, matchDto.getTeam2Dto().getPlayer2().getId());

		final Team dbTeam2 = getTeam(dbTeam2Player1, dbTeam2Player2);
		matchDto.getTeam2Dto().setId(dbTeam2.getKey().getId());

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

	private boolean isTeam1Winner(MatchDto matchDto) {
		boolean team1Winner = false;
		int size = 0;
		for (Integer result : matchDto.getMatchSetsDto().getMatchSetsTeam1()) {
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

		final PlayerSingleStats dbPlayerSingleStats = PMFactory.getObjectById(PlayerSingleStats.class, dbPlayer.getPlayerSingleStats());

		final int matchPoints = MatchServiceHelper.getPointsForPlayer(winner, dbPlayer, dbPlayerSingleStats, matchDto);
		if (winner) {
			final int wins = dbPlayerSingleStats.getWins() + 1;
			dbPlayerSingleStats.setWins(wins);
		} else {
			final int losses = dbPlayerSingleStats.getLosses() + 1;
			dbPlayerSingleStats.setLosses(losses);
		}
		final int points = dbPlayerSingleStats.getPoints() + matchPoints;
		dbPlayerSingleStats.setPoints(points);

		final int goalsTeam1 = MatchServiceHelper.getGoalsTeam1(matchDto);
		final int goalsTeam2 = MatchServiceHelper.getGoalsTeam2(matchDto);
		if (matchDto.getTeam1Dto().getPlayer1().getId() == playerId || matchDto.getTeam1Dto().getPlayer2() != null
				&& matchDto.getTeam1Dto().getPlayer2().getId() == playerId) {
			dbMatch.getMatchPoints().getMatchPointsTeam1().add(matchPoints);

			final int shotGoals = dbPlayerSingleStats.getShotGoals() + goalsTeam1;
			dbPlayerSingleStats.setShotGoals(shotGoals);

			final int getGoals = dbPlayerSingleStats.getGetGoals() + goalsTeam2;
			dbPlayerSingleStats.setGetGoals(getGoals);
		} else if (matchDto.getTeam2Dto().getPlayer1().getId() == playerId || matchDto.getTeam2Dto().getPlayer2() != null
				&& matchDto.getTeam2Dto().getPlayer2().getId() == playerId) {
			dbMatch.getMatchPoints().getMatchPointsTeam2().add(matchPoints);

			final int shotGoals = dbPlayerSingleStats.getShotGoals() + goalsTeam2;
			dbPlayerSingleStats.setShotGoals(shotGoals);

			final int getGoals = dbPlayerSingleStats.getGetGoals() + goalsTeam1;
			dbPlayerSingleStats.setGetGoals(getGoals);
		}
		dbPlayerSingleStats.setLastMatchPoints(matchPoints);
		if (dbPlayer.getLastMatchDate() == null || matchDto.getMatchDate().after(dbPlayer.getLastMatchDate())) {
			dbPlayer.setLastMatchDate(matchDto.getMatchDate());
		}
		PMFactory.persistAllObjects(dbPlayerSingleStats, dbPlayer);
	}

	private void updatePlayerStatsForDoubleMatch(Player dbPlayer, MatchDto matchDto, Match dbMatch, boolean winner) {
		final long playerId = dbPlayer.getKey().getId();

		final PlayerDoubleStats dbPlayerDoubleStats = PMFactory.getObjectById(PlayerDoubleStats.class, dbPlayer.getPlayerDoubleStats());

		final int matchPoints = MatchServiceHelper.getPointsForPlayer(winner, dbPlayer, dbPlayerDoubleStats, matchDto);
		if (winner) {
			final int wins = dbPlayerDoubleStats.getWins() + 1;
			dbPlayerDoubleStats.setWins(wins);
		} else {
			final int losses = dbPlayerDoubleStats.getLosses() + 1;
			dbPlayerDoubleStats.setLosses(losses);
		}
		final int points = dbPlayerDoubleStats.getPoints() + matchPoints;
		dbPlayerDoubleStats.setPoints(points);

		final int goalsTeam1 = MatchServiceHelper.getGoalsTeam1(matchDto);
		final int goalsTeam2 = MatchServiceHelper.getGoalsTeam2(matchDto);
		if (matchDto.getTeam1Dto().getPlayer1().getId() == playerId || matchDto.getTeam1Dto().getPlayer2() != null
				&& matchDto.getTeam1Dto().getPlayer2().getId() == playerId) {
			dbMatch.getMatchPoints().getMatchPointsTeam1().add(matchPoints);

			final int shotGoals = dbPlayerDoubleStats.getShotGoals() + goalsTeam1;
			dbPlayerDoubleStats.setShotGoals(shotGoals);

			final int getGoals = dbPlayerDoubleStats.getGetGoals() + goalsTeam2;
			dbPlayerDoubleStats.setGetGoals(getGoals);
		} else if (matchDto.getTeam2Dto().getPlayer1().getId() == playerId || matchDto.getTeam2Dto().getPlayer2() != null
				&& matchDto.getTeam2Dto().getPlayer2().getId() == playerId) {
			dbMatch.getMatchPoints().getMatchPointsTeam2().add(matchPoints);

			final int shotGoals = dbPlayerDoubleStats.getShotGoals() + goalsTeam2;
			dbPlayerDoubleStats.setShotGoals(shotGoals);

			final int getGoals = dbPlayerDoubleStats.getGetGoals() + goalsTeam1;
			dbPlayerDoubleStats.setGetGoals(getGoals);
		}
		dbPlayerDoubleStats.setLastMatchPoints(matchPoints);
		if (dbPlayer.getLastMatchDate() == null || matchDto.getMatchDate().after(dbPlayer.getLastMatchDate())) {
			dbPlayer.setLastMatchDate(matchDto.getMatchDate());
		}
		PMFactory.persistAllObjects(dbPlayerDoubleStats, dbPlayer);
	}

	private void updateTeamStats(Team dbTeam, MatchDto matchDto, Match dbMatch, boolean winner) {
		final TeamStats dbTeamStats = PMFactory.getObjectById(TeamStats.class, dbTeam.getTeamStats());

		final int matchPoints = MatchServiceHelper.getPointsForTeam(winner, dbTeam, dbTeamStats, matchDto);
		if (winner) {
			final int wins = dbTeamStats.getWins() + 1;
			dbTeamStats.setWins(wins);
		} else {
			final int losses = dbTeamStats.getLosses() + 1;
			dbTeamStats.setLosses(losses);
		}
		final int points = dbTeamStats.getPoints() + matchPoints;
		dbTeamStats.setPoints(points);

		final int goalsTeam1 = MatchServiceHelper.getGoalsTeam1(matchDto);
		final int goalsTeam2 = MatchServiceHelper.getGoalsTeam2(matchDto);
		if (dbTeam.getPlayers().contains(matchDto.getTeam1Dto().getPlayer1().getId())
				|| dbTeam.getPlayers().contains(matchDto.getTeam1Dto().getPlayer2().getId())) {
			dbMatch.getMatchPoints().getMatchPointsTeam1().add(matchPoints);

			final int shotGoals = dbTeamStats.getShotGoals() + goalsTeam1;
			dbTeamStats.setShotGoals(shotGoals);

			final int getGoals = dbTeamStats.getGetGoals() + goalsTeam2;
			dbTeamStats.setGetGoals(getGoals);
		} else if (dbTeam.getPlayers().contains(matchDto.getTeam2Dto().getPlayer1().getId())
				|| dbTeam.getPlayers().contains(matchDto.getTeam2Dto().getPlayer2().getId())) {
			dbMatch.getMatchPoints().getMatchPointsTeam2().add(matchPoints);

			final int shotGoals = dbTeamStats.getShotGoals() + goalsTeam2;
			dbTeamStats.setShotGoals(shotGoals);

			final int getGoals = dbTeamStats.getGetGoals() + goalsTeam1;
			dbTeamStats.setGetGoals(getGoals);
		}
		dbTeamStats.setLastMatchPoints(matchPoints);
		if (dbTeam.getLastMatchDate() == null || matchDto.getMatchDate().after(dbTeam.getLastMatchDate())) {
			dbTeam.setLastMatchDate(matchDto.getMatchDate());
		}
		PMFactory.persistAllObjects(dbTeamStats, dbTeam);
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
			final MatchDto matchDto = MatchServiceHelper.createDtoMatch(dbMatch);

			matches.add(matchDto);
		}
		return matches;
	}

}
