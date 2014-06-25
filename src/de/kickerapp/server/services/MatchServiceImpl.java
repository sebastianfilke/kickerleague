package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.MatchService;
import de.kickerapp.server.dao.DoubleMatch;
import de.kickerapp.server.dao.Match;
import de.kickerapp.server.dao.Match.MatchSets;
import de.kickerapp.server.dao.MatchComment;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.PlayerDoubleStats;
import de.kickerapp.server.dao.PlayerSingleStats;
import de.kickerapp.server.dao.SingleMatch;
import de.kickerapp.server.dao.Team;
import de.kickerapp.server.dao.TeamStats;
import de.kickerapp.server.dao.fetchplans.MatchPlan;
import de.kickerapp.server.dao.fetchplans.PlayerPlan;
import de.kickerapp.server.dao.fetchplans.TeamPlan;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.persistence.queries.MatchBean;
import de.kickerapp.server.services.MatchServiceHelper.MatchDescendingComparator;
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
	public MatchDto createSingleMatch(MatchDto matchDto) throws IllegalArgumentException {
		final SingleMatch dbMatch = new SingleMatch();

		final int matchId = PMFactory.getNextId(SingleMatch.class.getName());
		final Key matchKey = KeyFactory.createKey(SingleMatch.class.getSimpleName(), matchId);
		dbMatch.setKey(matchKey);
		dbMatch.setMatchDate(matchDto.getMatchDate());
		dbMatch.setMatchNumber(MatchBean.getNextMatchNumber());

		final MatchSets sets = new MatchSets(matchDto.getMatchSetsDto().getMatchSetsTeam1(), matchDto.getMatchSetsDto().getMatchSetsTeam2());
		dbMatch.setMatchSets(sets);

		final Player dbTeam1Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam1Dto().getPlayer1().getId(), PlayerPlan.PLAYERSINGLESTATS);
		final Player dbTeam2Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam2Dto().getPlayer1().getId(), PlayerPlan.PLAYERSINGLESTATS);

		dbMatch.setPlayer1(dbTeam1Player1);
		dbMatch.setPlayer2(dbTeam2Player1);

		final boolean team1Winner = MatchServiceHelper.isTeam1Winner(dbMatch);
		updatePlayerSingleStats(dbTeam1Player1, dbMatch, team1Winner);
		updatePlayerSingleStats(dbTeam2Player1, dbMatch, !team1Winner);

		if (matchDto.getMatchCommentDto() != null) {
			createMatchComment(matchDto, dbMatch);
		}

		PMFactory.persistObject(dbMatch);
		MatchServiceHelper.updateTable(matchDto);

		return matchDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MatchDto createDoubleMatch(MatchDto matchDto) throws IllegalArgumentException {
		final DoubleMatch dbMatch = new DoubleMatch();

		final int matchId = PMFactory.getNextId(DoubleMatch.class.getName());
		final Key matchKey = KeyFactory.createKey(DoubleMatch.class.getSimpleName(), matchId);
		dbMatch.setKey(matchKey);
		dbMatch.setMatchDate(matchDto.getMatchDate());
		dbMatch.setMatchNumber(MatchBean.getNextMatchNumber());

		final MatchSets sets = new MatchSets(matchDto.getMatchSetsDto().getMatchSetsTeam1(), matchDto.getMatchSetsDto().getMatchSetsTeam2());
		dbMatch.setMatchSets(sets);

		final Player dbTeam1Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam1Dto().getPlayer1().getId(), PlayerPlan.PLAYERDOUBLESTATS,
				PlayerPlan.TEAMS);
		final Player dbTeam1Player2 = PMFactory.getObjectById(Player.class, matchDto.getTeam1Dto().getPlayer2().getId(), PlayerPlan.PLAYERDOUBLESTATS,
				PlayerPlan.TEAMS);
		final Team dbTeam1 = TeamServiceHelper.getTeam(dbTeam1Player1, dbTeam1Player2);

		final Player dbTeam2Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam2Dto().getPlayer1().getId(), PlayerPlan.PLAYERDOUBLESTATS,
				PlayerPlan.TEAMS);
		final Player dbTeam2Player2 = PMFactory.getObjectById(Player.class, matchDto.getTeam2Dto().getPlayer2().getId(), PlayerPlan.PLAYERDOUBLESTATS,
				PlayerPlan.TEAMS);
		final Team dbTeam2 = TeamServiceHelper.getTeam(dbTeam2Player1, dbTeam2Player2);

		dbMatch.setTeam1(dbTeam1);
		dbMatch.setTeam2(dbTeam2);

		final boolean team1Winner = MatchServiceHelper.isTeam1Winner(dbMatch);
		updatePlayerDoubleStats(dbTeam1.getPlayer1(), dbMatch, team1Winner);
		updatePlayerDoubleStats(dbTeam1.getPlayer2(), dbMatch, team1Winner);
		updatePlayerDoubleStats(dbTeam2.getPlayer1(), dbMatch, !team1Winner);
		updatePlayerDoubleStats(dbTeam2.getPlayer2(), dbMatch, !team1Winner);

		updateTeamStats(dbTeam1, dbMatch, team1Winner);
		updateTeamStats(dbTeam2, dbMatch, !team1Winner);

		if (matchDto.getMatchCommentDto() != null) {
			createMatchComment(matchDto, dbMatch);
		}

		PMFactory.persistObject(dbMatch);
		MatchServiceHelper.updateTable(matchDto);

		return matchDto;
	}

	/**
	 * Aktualisiert die Einzelspielstatistik.
	 * 
	 * @param dbPlayer Der Spieler.
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param winner <code>true</code> falls der Spieler gewonnen hat, andernfalls <code>false</code>.
	 */
	private void updatePlayerSingleStats(Player dbPlayer, SingleMatch dbMatch, boolean winner) {
		final PlayerSingleStats dbPlayerSingleStats = dbPlayer.getPlayerSingleStats();

		final int matchPoints = MatchServiceHelper.getPointsForSinglePlayer(dbPlayer, dbMatch, winner);
		if (winner) {
			final int wins = dbPlayerSingleStats.getWins() + 1;
			dbPlayerSingleStats.setWins(wins);
		} else {
			final int defeats = dbPlayerSingleStats.getDefeats() + 1;
			dbPlayerSingleStats.setDefeats(defeats);
		}
		final int points = dbPlayerSingleStats.getPoints() + matchPoints;
		dbPlayerSingleStats.setPoints(points);

		final int winSetsTeam1 = MatchServiceHelper.getWinSetsTeam1(dbMatch);
		final int winSetsTeam2 = MatchServiceHelper.getWinSetsTeam2(dbMatch);
		final int goalsTeam1 = MatchServiceHelper.getGoalsTeam1(dbMatch);
		final int goalsTeam2 = MatchServiceHelper.getGoalsTeam2(dbMatch);
		if (dbMatch.getPlayer1() == dbPlayer) {
			dbMatch.getMatchPoints().getMatchPointsTeam1().add(matchPoints);

			final int winSets = dbPlayerSingleStats.getWinSets() + winSetsTeam1;
			dbPlayerSingleStats.setWinSets(winSets);

			final int lostSets = dbPlayerSingleStats.getLostSets() + winSetsTeam2;
			dbPlayerSingleStats.setLostSets(lostSets);

			final int shotGoals = dbPlayerSingleStats.getShotGoals() + goalsTeam1;
			dbPlayerSingleStats.setShotGoals(shotGoals);

			final int getGoals = dbPlayerSingleStats.getGetGoals() + goalsTeam2;
			dbPlayerSingleStats.setGetGoals(getGoals);
		} else if (dbMatch.getPlayer2() == dbPlayer) {
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
		if (dbPlayer.getLastMatchDate() == null || dbMatch.getMatchDate().after(dbPlayer.getLastMatchDate())) {
			dbPlayer.setLastMatchDate(dbMatch.getMatchDate());
		}
	}

	/**
	 * Aktualisiert die Doppelspielstatistik.
	 * 
	 * @param dbPlayer Der Spieler.
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param winner <code>true</code> falls der Spieler gewonnen hat, andernfalls <code>false</code>.
	 */
	private void updatePlayerDoubleStats(Player dbPlayer, DoubleMatch dbMatch, boolean winner) {
		final PlayerDoubleStats dbPlayerDoubleStats = dbPlayer.getPlayerDoubleStats();

		final int matchPoints = MatchServiceHelper.getPointsForDoublePlayer(dbPlayer, dbMatch, winner);
		if (winner) {
			final int wins = dbPlayerDoubleStats.getWins() + 1;
			dbPlayerDoubleStats.setWins(wins);
		} else {
			final int defeats = dbPlayerDoubleStats.getDefeats() + 1;
			dbPlayerDoubleStats.setDefeats(defeats);
		}
		final int points = dbPlayerDoubleStats.getPoints() + matchPoints;
		dbPlayerDoubleStats.setPoints(points);

		final int winSetsTeam1 = MatchServiceHelper.getWinSetsTeam1(dbMatch);
		final int winSetsTeam2 = MatchServiceHelper.getWinSetsTeam2(dbMatch);
		final int goalsTeam1 = MatchServiceHelper.getGoalsTeam1(dbMatch);
		final int goalsTeam2 = MatchServiceHelper.getGoalsTeam2(dbMatch);
		if (dbMatch.getTeam1().getPlayer1() == dbPlayer || dbMatch.getTeam1().getPlayer2() == dbPlayer) {
			dbMatch.getMatchPoints().getMatchPointsTeam1().add(matchPoints);

			final int winSets = dbPlayerDoubleStats.getWinSets() + winSetsTeam1;
			dbPlayerDoubleStats.setWinSets(winSets);

			final int lostSets = dbPlayerDoubleStats.getLostSets() + winSetsTeam2;
			dbPlayerDoubleStats.setLostSets(lostSets);

			final int shotGoals = dbPlayerDoubleStats.getShotGoals() + goalsTeam1;
			dbPlayerDoubleStats.setShotGoals(shotGoals);

			final int getGoals = dbPlayerDoubleStats.getGetGoals() + goalsTeam2;
			dbPlayerDoubleStats.setGetGoals(getGoals);
		} else if (dbMatch.getTeam2().getPlayer1() == dbPlayer || dbMatch.getTeam2().getPlayer2() == dbPlayer) {
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
		if (dbPlayer.getLastMatchDate() == null || dbMatch.getMatchDate().after(dbPlayer.getLastMatchDate())) {
			dbPlayer.setLastMatchDate(dbMatch.getMatchDate());
		}
	}

	/**
	 * Aktualisiert die Teamspielstatistik.
	 * 
	 * @param dbTeam Das Team.
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param winner <code>true</code> falls das Team gewonnen hat, andernfalls <code>false</code>.
	 */
	private void updateTeamStats(Team dbTeam, DoubleMatch dbMatch, boolean winner) {
		final TeamStats dbTeamStats = dbTeam.getTeamStats();

		final int matchPoints = MatchServiceHelper.getPointsForTeam(winner, dbTeam, dbMatch);
		if (winner) {
			final int wins = dbTeamStats.getWins() + 1;
			dbTeamStats.setWins(wins);
		} else {
			final int defeats = dbTeamStats.getDefeats() + 1;
			dbTeamStats.setDefeats(defeats);
		}
		final int points = dbTeamStats.getPoints() + matchPoints;
		dbTeamStats.setPoints(points);

		final int winSetsTeam1 = MatchServiceHelper.getWinSetsTeam1(dbMatch);
		final int winSetsTeam2 = MatchServiceHelper.getWinSetsTeam2(dbMatch);
		final int goalsTeam1 = MatchServiceHelper.getGoalsTeam1(dbMatch);
		final int goalsTeam2 = MatchServiceHelper.getGoalsTeam2(dbMatch);
		if (dbMatch.getTeam1() == dbTeam) {
			dbMatch.getMatchPoints().getMatchPointsTeam1().add(matchPoints);

			final int winSets = dbTeamStats.getWinSets() + winSetsTeam1;
			dbTeamStats.setWinSets(winSets);

			final int lostSets = dbTeamStats.getLostSets() + winSetsTeam2;
			dbTeamStats.setLostSets(lostSets);

			final int shotGoals = dbTeamStats.getShotGoals() + goalsTeam1;
			dbTeamStats.setShotGoals(shotGoals);

			final int getGoals = dbTeamStats.getGetGoals() + goalsTeam2;
			dbTeamStats.setGetGoals(getGoals);
		} else if (dbMatch.getTeam2() == dbTeam) {
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
		if (dbTeam.getLastMatchDate() == null || dbMatch.getMatchDate().after(dbTeam.getLastMatchDate())) {
			dbTeam.setLastMatchDate(dbMatch.getMatchDate());
		}
	}

	/**
	 * Erzeugt und Speichert ein Kommentar zum Spiel.
	 * 
	 * @param matchDto Die Client-Datenklasse.
	 * @param dbMatch Die Objekt-Datenklasse.
	 */
	private void createMatchComment(final MatchDto matchDto, final Match dbMatch) {
		final MatchComment dbComment = new MatchComment(matchDto.getMatchCommentDto().getComment());
		final int commentId = PMFactory.getNextId(MatchComment.class.getName());
		final Key commentKey = KeyFactory.createKey(dbMatch.getKey(), MatchComment.class.getSimpleName(), commentId);
		dbComment.setKey(commentKey);

		dbMatch.setMatchComment(dbComment);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<MatchDto> getAllMatches() throws IllegalArgumentException {
		final ArrayList<MatchDto> matches = new ArrayList<MatchDto>();

		final List<SingleMatch> dbSingleMatches = PMFactory.getList(SingleMatch.class, MatchPlan.COMMENT, MatchPlan.BOTHPLAYERS);
		final List<DoubleMatch> dbDoubleMatches = PMFactory.getList(DoubleMatch.class, MatchPlan.COMMENT, MatchPlan.BOTHTEAMS, TeamPlan.BOTHPLAYERS);

		Collections.sort(dbSingleMatches, new MatchDescendingComparator());
		Collections.sort(dbDoubleMatches, new MatchDescendingComparator());
		for (SingleMatch dbMatch : dbSingleMatches) {
			final MatchDto matchDto = MatchServiceHelper.createSingleMatchDto(dbMatch);

			matches.add(matchDto);
		}
		for (DoubleMatch dbMatch : dbDoubleMatches) {
			final MatchDto matchDto = MatchServiceHelper.createDoubleMatchDto(dbMatch);

			matches.add(matchDto);
		}
		return matches;
	}

}
