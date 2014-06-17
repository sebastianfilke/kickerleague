package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.MatchService;
import de.kickerapp.server.dao.Match;
import de.kickerapp.server.dao.Match.MatchSets;
import de.kickerapp.server.dao.MatchComment;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.PlayerDoubleStats;
import de.kickerapp.server.dao.PlayerSingleStats;
import de.kickerapp.server.dao.Stats;
import de.kickerapp.server.dao.Team;
import de.kickerapp.server.dao.TeamStats;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.persistence.queries.MatchBean;
import de.kickerapp.server.services.MatchServiceHelper.MatchDescendingComparator;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.MatchDto;

/**
 * Dienst zur Verarbeitung von Spielen im Klienten.
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
		if (matchDto.getMatchCommentDto() != null) {
			createMatchComment(matchDto, dbMatch);
		}

		PMFactory.persistObject(dbMatch);
		MatchServiceHelper.updateTable(matchDto);

		return matchDto;
	}

	/**
	 * Erzeugt und Speichert ein Einzelspiel.
	 * 
	 * @param matchDto Die Client-Datenklasse.
	 * @param dbMatch Die Objekt-Datenklasse.
	 */
	private void createSingleMatch(final MatchDto matchDto, Match dbMatch) {
		dbMatch.setMatchType(MatchType.SINGLE);

		final Player dbTeam1Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam1Dto().getPlayer1().getId());
		final Player dbTeam2Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam2Dto().getPlayer1().getId());

		dbMatch.setTeam1(dbTeam1Player1.getKey().getId());
		dbMatch.setTeam2(dbTeam2Player1.getKey().getId());

		final boolean team1Winner = MatchServiceHelper.isTeam1Winner(matchDto);
		final PlayerSingleStats dbTeam1Player1SingleStats = (PlayerSingleStats) updatePlayerStats(dbTeam1Player1, matchDto, dbMatch, team1Winner);
		final PlayerSingleStats dbTeam2Player1SingleStats = (PlayerSingleStats) updatePlayerStats(dbTeam2Player1, matchDto, dbMatch, !team1Winner);

		PMFactory.persistAllObjects(dbTeam1Player1SingleStats, dbTeam1Player1);
		PMFactory.persistAllObjects(dbTeam2Player1SingleStats, dbTeam2Player1);
	}

	/**
	 * Erzeugt und Speichert ein Doppelspiel.
	 * 
	 * @param matchDto Die Client-Datenklasse.
	 * @param dbMatch Die Objekt-Datenklasse.
	 */
	private void createDoubleMatch(final MatchDto matchDto, final Match dbMatch) {
		dbMatch.setMatchType(MatchType.DOUBLE);

		final Player dbTeam1Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam1Dto().getPlayer1().getId());
		final Player dbTeam1Player2 = PMFactory.getObjectById(Player.class, matchDto.getTeam1Dto().getPlayer2().getId());

		final Team dbTeam1 = TeamServiceHelper.getTeam(dbTeam1Player1, dbTeam1Player2);
		matchDto.getTeam1Dto().setId(dbTeam1.getKey().getId());

		final Player dbTeam1Player1Sorted = MatchServiceHelper.getPlayerForTeamId(dbTeam1Player1, dbTeam1Player2, (Long) dbTeam1.getPlayers().toArray()[0]);
		final Player dbTeam1Player2Sorted = MatchServiceHelper.getPlayerForTeamId(dbTeam1Player1, dbTeam1Player2, (Long) dbTeam1.getPlayers().toArray()[1]);

		final Player dbTeam2Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam2Dto().getPlayer1().getId());
		final Player dbTeam2Player2 = PMFactory.getObjectById(Player.class, matchDto.getTeam2Dto().getPlayer2().getId());

		final Team dbTeam2 = TeamServiceHelper.getTeam(dbTeam2Player1, dbTeam2Player2);
		matchDto.getTeam2Dto().setId(dbTeam2.getKey().getId());

		final Player dbTeam2Player1Sorted = MatchServiceHelper.getPlayerForTeamId(dbTeam2Player1, dbTeam2Player2, (Long) dbTeam2.getPlayers().toArray()[0]);
		final Player dbTeam2Player2Sorted = MatchServiceHelper.getPlayerForTeamId(dbTeam2Player1, dbTeam2Player2, (Long) dbTeam2.getPlayers().toArray()[1]);

		dbMatch.setTeam1(dbTeam1.getKey().getId());
		dbMatch.setTeam2(dbTeam2.getKey().getId());

		final boolean team1Winner = MatchServiceHelper.isTeam1Winner(matchDto);
		final PlayerDoubleStats dbTeam1Player1DoubleStats = (PlayerDoubleStats) updatePlayerStats(dbTeam1Player1Sorted, matchDto, dbMatch, team1Winner);
		final PlayerDoubleStats dbTeam1Player2DoubleStats = (PlayerDoubleStats) updatePlayerStats(dbTeam1Player2Sorted, matchDto, dbMatch, team1Winner);
		final PlayerDoubleStats dbTeam2Player1DoubleStats = (PlayerDoubleStats) updatePlayerStats(dbTeam2Player1Sorted, matchDto, dbMatch, !team1Winner);
		final PlayerDoubleStats dbTeam2Player2DoubleStats = (PlayerDoubleStats) updatePlayerStats(dbTeam2Player2Sorted, matchDto, dbMatch, !team1Winner);

		PMFactory.persistAllObjects(dbTeam1Player1DoubleStats, dbTeam1Player1Sorted);
		PMFactory.persistAllObjects(dbTeam1Player2DoubleStats, dbTeam1Player2Sorted);
		PMFactory.persistAllObjects(dbTeam2Player1DoubleStats, dbTeam2Player1Sorted);
		PMFactory.persistAllObjects(dbTeam2Player2DoubleStats, dbTeam2Player2Sorted);

		final TeamStats dbTeam1TeamStats = (TeamStats) updateTeamStats(dbTeam1, matchDto, dbMatch, team1Winner);
		final TeamStats dbTeam2TeamStats = (TeamStats) updateTeamStats(dbTeam2, matchDto, dbMatch, !team1Winner);

		PMFactory.persistAllObjects(dbTeam1TeamStats, dbTeam1);
		PMFactory.persistAllObjects(dbTeam2TeamStats, dbTeam2);
	}

	/**
	 * Liefert die aktualisierte Einzelspiel- bzw. Doppelspielstatistik.
	 * 
	 * @param dbPlayer Der Spieler.
	 * @param matchDto Das Spiel.
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param winner <code>true</code> falls der Spieler gewonnen hat, andernfalls <code>false</code>.
	 * @return Die aktualisierte Einzelspiel- bzw. Doppelspielstatistik.
	 */
	private Stats updatePlayerStats(Player dbPlayer, MatchDto matchDto, Match dbMatch, boolean winner) {
		Stats playerStats = null;
		if (matchDto.getMatchType() == MatchType.SINGLE) {
			playerStats = updatePlayerStatsForSingleMatch(dbPlayer, matchDto, dbMatch, winner);
		} else {
			playerStats = updatePlayerStatsForDoubleMatch(dbPlayer, matchDto, dbMatch, winner);
		}
		return playerStats;
	}

	/**
	 * Liefert die aktualisierte Einzelspielstatistik.
	 * 
	 * @param dbPlayer Der Spieler.
	 * @param matchDto Das Spiel.
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param winner <code>true</code> falls der Spieler gewonnen hat, andernfalls <code>false</code>.
	 * @return Die aktualisierte Einzelspielstatistik.
	 */
	private PlayerSingleStats updatePlayerStatsForSingleMatch(Player dbPlayer, MatchDto matchDto, Match dbMatch, boolean winner) {
		final long playerId = dbPlayer.getKey().getId();

		final PlayerSingleStats dbPlayerSingleStats = PMFactory.getObjectById(PlayerSingleStats.class, dbPlayer.getPlayerSingleStats());

		final int matchPoints = MatchServiceHelper.getPointsForPlayer(winner, dbPlayer, dbPlayerSingleStats, matchDto);
		if (winner) {
			final int wins = dbPlayerSingleStats.getWins() + 1;
			dbPlayerSingleStats.setWins(wins);
		} else {
			final int defeats = dbPlayerSingleStats.getDefeats() + 1;
			dbPlayerSingleStats.setDefeats(defeats);
		}
		final int points = dbPlayerSingleStats.getPoints() + matchPoints;
		dbPlayerSingleStats.setPoints(points);

		final int winSetsTeam1 = MatchServiceHelper.getWinSetsTeam1(matchDto);
		final int winSetsTeam2 = MatchServiceHelper.getWinSetsTeam2(matchDto);
		final int goalsTeam1 = MatchServiceHelper.getGoalsTeam1(matchDto);
		final int goalsTeam2 = MatchServiceHelper.getGoalsTeam2(matchDto);
		if (matchDto.getTeam1Dto().getPlayer1().getId() == playerId || matchDto.getTeam1Dto().getPlayer2() != null
				&& matchDto.getTeam1Dto().getPlayer2().getId() == playerId) {
			dbMatch.getMatchPoints().getMatchPointsTeam1().add(matchPoints);

			final int winSets = dbPlayerSingleStats.getWinSets() + winSetsTeam1;
			dbPlayerSingleStats.setWinSets(winSets);

			final int lostSets = dbPlayerSingleStats.getLostSets() + winSetsTeam2;
			dbPlayerSingleStats.setLostSets(lostSets);

			final int shotGoals = dbPlayerSingleStats.getShotGoals() + goalsTeam1;
			dbPlayerSingleStats.setShotGoals(shotGoals);

			final int getGoals = dbPlayerSingleStats.getGetGoals() + goalsTeam2;
			dbPlayerSingleStats.setGetGoals(getGoals);
		} else if (matchDto.getTeam2Dto().getPlayer1().getId() == playerId || matchDto.getTeam2Dto().getPlayer2() != null
				&& matchDto.getTeam2Dto().getPlayer2().getId() == playerId) {
			dbMatch.getMatchPoints().getMatchPointsTeam2().add(matchPoints);

			final int winSets = dbPlayerSingleStats.getWinSets() + winSetsTeam2;
			dbPlayerSingleStats.setWinSets(winSets);

			final int lostSets = dbPlayerSingleStats.getLostSets() + winSetsTeam1;
			dbPlayerSingleStats.setLostSets(lostSets);

			final int shotGoals = dbPlayerSingleStats.getShotGoals() + goalsTeam2;
			dbPlayerSingleStats.setShotGoals(shotGoals);

			final int getGoals = dbPlayerSingleStats.getGetGoals() + goalsTeam1;
			dbPlayerSingleStats.setGetGoals(getGoals);
		}
		dbPlayerSingleStats.setLastMatchPoints(matchPoints);
		if (dbPlayer.getLastMatchDate() == null || matchDto.getMatchDate().after(dbPlayer.getLastMatchDate())) {
			dbPlayer.setLastMatchDate(matchDto.getMatchDate());
		}
		return dbPlayerSingleStats;
	}

	/**
	 * Liefert die aktualisierte Doppelspielstatistik.
	 * 
	 * @param dbPlayer Der Spieler.
	 * @param matchDto Das Spiel.
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param winner <code>true</code> falls der Spieler gewonnen hat, andernfalls <code>false</code>.
	 * @return Die aktualisierte Doppelspielstatistik.
	 */
	private PlayerDoubleStats updatePlayerStatsForDoubleMatch(Player dbPlayer, MatchDto matchDto, Match dbMatch, boolean winner) {
		final long playerId = dbPlayer.getKey().getId();

		final PlayerDoubleStats dbPlayerDoubleStats = PMFactory.getObjectById(PlayerDoubleStats.class, dbPlayer.getPlayerDoubleStats());

		final int matchPoints = MatchServiceHelper.getPointsForPlayer(winner, dbPlayer, dbPlayerDoubleStats, matchDto);
		if (winner) {
			final int wins = dbPlayerDoubleStats.getWins() + 1;
			dbPlayerDoubleStats.setWins(wins);
		} else {
			final int defeats = dbPlayerDoubleStats.getDefeats() + 1;
			dbPlayerDoubleStats.setDefeats(defeats);
		}
		final int points = dbPlayerDoubleStats.getPoints() + matchPoints;
		dbPlayerDoubleStats.setPoints(points);

		final int winSetsTeam1 = MatchServiceHelper.getWinSetsTeam1(matchDto);
		final int winSetsTeam2 = MatchServiceHelper.getWinSetsTeam2(matchDto);
		final int goalsTeam1 = MatchServiceHelper.getGoalsTeam1(matchDto);
		final int goalsTeam2 = MatchServiceHelper.getGoalsTeam2(matchDto);
		if (matchDto.getTeam1Dto().getPlayer1().getId() == playerId || matchDto.getTeam1Dto().getPlayer2() != null
				&& matchDto.getTeam1Dto().getPlayer2().getId() == playerId) {
			dbMatch.getMatchPoints().getMatchPointsTeam1().add(matchPoints);

			final int winSets = dbPlayerDoubleStats.getWinSets() + winSetsTeam1;
			dbPlayerDoubleStats.setWinSets(winSets);

			final int lostSets = dbPlayerDoubleStats.getLostSets() + winSetsTeam2;
			dbPlayerDoubleStats.setLostSets(lostSets);

			final int shotGoals = dbPlayerDoubleStats.getShotGoals() + goalsTeam1;
			dbPlayerDoubleStats.setShotGoals(shotGoals);

			final int getGoals = dbPlayerDoubleStats.getGetGoals() + goalsTeam2;
			dbPlayerDoubleStats.setGetGoals(getGoals);
		} else if (matchDto.getTeam2Dto().getPlayer1().getId() == playerId || matchDto.getTeam2Dto().getPlayer2() != null
				&& matchDto.getTeam2Dto().getPlayer2().getId() == playerId) {
			dbMatch.getMatchPoints().getMatchPointsTeam2().add(matchPoints);

			final int winSets = dbPlayerDoubleStats.getWinSets() + winSetsTeam2;
			dbPlayerDoubleStats.setWinSets(winSets);

			final int lostSets = dbPlayerDoubleStats.getLostSets() + winSetsTeam1;
			dbPlayerDoubleStats.setLostSets(lostSets);

			final int shotGoals = dbPlayerDoubleStats.getShotGoals() + goalsTeam2;
			dbPlayerDoubleStats.setShotGoals(shotGoals);

			final int getGoals = dbPlayerDoubleStats.getGetGoals() + goalsTeam1;
			dbPlayerDoubleStats.setGetGoals(getGoals);
		}
		dbPlayerDoubleStats.setLastMatchPoints(matchPoints);
		if (dbPlayer.getLastMatchDate() == null || matchDto.getMatchDate().after(dbPlayer.getLastMatchDate())) {
			dbPlayer.setLastMatchDate(matchDto.getMatchDate());
		}
		return dbPlayerDoubleStats;
	}

	/**
	 * Liefert die aktualisierte Teamspielstatistik.
	 * 
	 * @param dbTeam Das Team.
	 * @param matchDto Das Spiel.
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param winner <code>true</code> falls das Team gewonnen hat, andernfalls <code>false</code>.
	 * @return Die aktualisierte Teamspielstatistik.
	 */
	private TeamStats updateTeamStats(Team dbTeam, MatchDto matchDto, Match dbMatch, boolean winner) {
		final TeamStats dbTeamStats = PMFactory.getObjectById(TeamStats.class, dbTeam.getTeamStats());

		final int matchPoints = MatchServiceHelper.getPointsForTeam(winner, dbTeam, dbTeamStats, matchDto);
		if (winner) {
			final int wins = dbTeamStats.getWins() + 1;
			dbTeamStats.setWins(wins);
		} else {
			final int defeats = dbTeamStats.getDefeats() + 1;
			dbTeamStats.setDefeats(defeats);
		}
		final int points = dbTeamStats.getPoints() + matchPoints;
		dbTeamStats.setPoints(points);

		final int winSetsTeam1 = MatchServiceHelper.getWinSetsTeam1(matchDto);
		final int winSetsTeam2 = MatchServiceHelper.getWinSetsTeam2(matchDto);
		final int goalsTeam1 = MatchServiceHelper.getGoalsTeam1(matchDto);
		final int goalsTeam2 = MatchServiceHelper.getGoalsTeam2(matchDto);
		if (dbTeam.getPlayers().contains(matchDto.getTeam1Dto().getPlayer1().getId())
				|| dbTeam.getPlayers().contains(matchDto.getTeam1Dto().getPlayer2().getId())) {
			dbMatch.getMatchPoints().getMatchPointsTeam1().add(matchPoints);

			final int winSets = dbTeamStats.getWinSets() + winSetsTeam1;
			dbTeamStats.setWinSets(winSets);

			final int lostSets = dbTeamStats.getLostSets() + winSetsTeam2;
			dbTeamStats.setLostSets(lostSets);

			final int shotGoals = dbTeamStats.getShotGoals() + goalsTeam1;
			dbTeamStats.setShotGoals(shotGoals);

			final int getGoals = dbTeamStats.getGetGoals() + goalsTeam2;
			dbTeamStats.setGetGoals(getGoals);
		} else if (dbTeam.getPlayers().contains(matchDto.getTeam2Dto().getPlayer1().getId())
				|| dbTeam.getPlayers().contains(matchDto.getTeam2Dto().getPlayer2().getId())) {
			dbMatch.getMatchPoints().getMatchPointsTeam2().add(matchPoints);

			final int winSets = dbTeamStats.getWinSets() + winSetsTeam2;
			dbTeamStats.setWinSets(winSets);

			final int lostSets = dbTeamStats.getLostSets() + winSetsTeam1;
			dbTeamStats.setLostSets(lostSets);

			final int shotGoals = dbTeamStats.getShotGoals() + goalsTeam2;
			dbTeamStats.setShotGoals(shotGoals);

			final int getGoals = dbTeamStats.getGetGoals() + goalsTeam1;
			dbTeamStats.setGetGoals(getGoals);
		}
		dbTeamStats.setLastMatchPoints(matchPoints);
		if (dbTeam.getLastMatchDate() == null || matchDto.getMatchDate().after(dbTeam.getLastMatchDate())) {
			dbTeam.setLastMatchDate(matchDto.getMatchDate());
		}
		return dbTeamStats;
	}

	/**
	 * Erzeugt und Speichert ein Kommentar zum Spiel.
	 * 
	 * @param matchDto Die Client-Datenklasse.
	 * @param dbMatch Die Objekt-Datenklasse.
	 */
	private void createMatchComment(final MatchDto matchDto, final Match dbMatch) {
		final int commentId = PMFactory.getNextId(MatchComment.class.getName());
		final Key commentKey = KeyFactory.createKey(dbMatch.getKey(), MatchComment.class.getSimpleName(), commentId);

		final MatchComment dbComment = new MatchComment(matchDto.getMatchCommentDto().getComment());
		dbComment.setKey(commentKey);

		dbMatch.setMatchComment(dbComment);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<MatchDto> getAllMatches() throws IllegalArgumentException {
		final ArrayList<MatchDto> matches = new ArrayList<MatchDto>();

		final List<Match> dbMatches = MatchBean.getAllMatches();
		final List<Player> dbPlayers = PMFactory.getList(Player.class);
		final List<Team> dbTeams = PMFactory.getList(Team.class);

		Collections.sort(dbMatches, new MatchDescendingComparator());
		for (Match dbMatch : dbMatches) {
			final MatchDto matchDto = MatchServiceHelper.createDtoMatch(dbMatch, dbPlayers, dbTeams);

			matches.add(matchDto);
		}
		return matches;
	}

}
