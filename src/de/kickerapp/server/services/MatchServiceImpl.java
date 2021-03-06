package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.MatchService;
import de.kickerapp.server.dao.DoubleMatch;
import de.kickerapp.server.dao.Match.MatchSets;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.PlayerDoubleStats;
import de.kickerapp.server.dao.PlayerSingleStats;
import de.kickerapp.server.dao.SingleMatch;
import de.kickerapp.server.dao.Team;
import de.kickerapp.server.dao.TeamStats;
import de.kickerapp.server.dao.fetchplans.PlayerPlan;
import de.kickerapp.server.persistence.JCacheFactory;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.persistence.queries.MatchBean;
import de.kickerapp.server.services.MatchServiceHelper.MatchDescendingComparator;
import de.kickerapp.shared.dto.MatchDto;
import de.kickerapp.shared.exception.KickerLeagueException;

/**
 * Dienst zur Verarbeitung von Spielen im Klienten.
 * 
 * @author Sebastian Filke
 */
public class MatchServiceImpl extends RemoteServiceServlet implements MatchService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MatchDto createSingleMatch(MatchDto matchDto) throws KickerLeagueException {
		final SingleMatch dbMatch = new SingleMatch();

		final int nextMatchNumber = MatchBean.getNextMatchNumber();
		final int matchId = PMFactory.getNextId(SingleMatch.class.getName());
		final Key matchKey = KeyFactory.createKey(SingleMatch.class.getSimpleName(), matchId);
		dbMatch.setKey(matchKey);
		dbMatch.setMatchDate(matchDto.getMatchDate());
		dbMatch.setMatchComment(matchDto.getMatchComment());
		dbMatch.setMatchNumber(nextMatchNumber);

		final MatchSets sets = new MatchSets(matchDto.getMatchSetsDto().getMatchSetsTeam1(), matchDto.getMatchSetsDto().getMatchSetsTeam2());
		dbMatch.setMatchSets(sets);

		final Player dbTeam1Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam1Dto().getPlayer1().getId(), PlayerPlan.PLAYERSINGLESTATS);
		final Player dbTeam2Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam2Dto().getPlayer1().getId(), PlayerPlan.PLAYERSINGLESTATS);

		dbMatch.setPlayer1(dbTeam1Player1);
		dbMatch.setPlayer2(dbTeam2Player1);

		final boolean team1Winner = MatchServiceHelper.isTeam1Winner(dbMatch);
		final int matchPointsPlayer1 = MatchServiceHelper.getPointsForSinglePlayer(dbTeam1Player1, dbMatch, team1Winner);
		final int matchPointsPlayer2 = MatchServiceHelper.getPointsForSinglePlayer(dbTeam2Player1, dbMatch, !team1Winner);
		updatePlayerSingleStats(dbTeam1Player1, dbMatch, matchPointsPlayer1, team1Winner);
		updatePlayerSingleStats(dbTeam2Player1, dbMatch, matchPointsPlayer2, !team1Winner);

		PMFactory.persistObject(dbMatch);
		MatchServiceHelper.updateTable(matchDto);
		MatchServiceHelper.createSingleMatchYearAggregation(dbMatch);
		MatchServiceHelper.createSingleMatchHistories(dbMatch, team1Winner);

		matchDto.setId(dbMatch.getKey().getId());

		// Lösche paginierte Spieler aus dem Cache
		JCacheFactory.get().remove(JCacheFactory.PAGEDPLAYERS);

		return matchDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MatchDto createDoubleMatch(MatchDto matchDto) throws KickerLeagueException {
		final DoubleMatch dbMatch = new DoubleMatch();

		final int nextMatchNumber = MatchBean.getNextMatchNumber();
		final int matchId = PMFactory.getNextId(DoubleMatch.class.getName());
		final Key matchKey = KeyFactory.createKey(DoubleMatch.class.getSimpleName(), matchId);
		dbMatch.setKey(matchKey);
		dbMatch.setMatchDate(matchDto.getMatchDate());
		dbMatch.setMatchComment(matchDto.getMatchComment());
		dbMatch.setMatchNumber(nextMatchNumber);

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
		final int matchPointsTeam1Player1 = MatchServiceHelper.getPointsForDoublePlayer(dbTeam1.getPlayer1(), dbMatch, team1Winner);
		final int matchPointsTeam1Player2 = MatchServiceHelper.getPointsForDoublePlayer(dbTeam1.getPlayer2(), dbMatch, team1Winner);
		final int matchPointsTeam2Player1 = MatchServiceHelper.getPointsForDoublePlayer(dbTeam2.getPlayer1(), dbMatch, !team1Winner);
		final int matchPointsTeam2Player2 = MatchServiceHelper.getPointsForDoublePlayer(dbTeam2.getPlayer2(), dbMatch, !team1Winner);
		updatePlayerDoubleStats(dbTeam1.getPlayer1(), dbMatch, matchPointsTeam1Player1, team1Winner);
		updatePlayerDoubleStats(dbTeam1.getPlayer2(), dbMatch, matchPointsTeam1Player2, team1Winner);
		updatePlayerDoubleStats(dbTeam2.getPlayer1(), dbMatch, matchPointsTeam2Player1, !team1Winner);
		updatePlayerDoubleStats(dbTeam2.getPlayer2(), dbMatch, matchPointsTeam2Player2, !team1Winner);

		final int matchPointsTeam1 = MatchServiceHelper.getPointsForTeam(dbTeam1, dbMatch, team1Winner);
		final int matchPointsTeam2 = MatchServiceHelper.getPointsForTeam(dbTeam2, dbMatch, !team1Winner);
		updateTeamStats(dbTeam1, dbMatch, matchPointsTeam1, team1Winner);
		updateTeamStats(dbTeam2, dbMatch, matchPointsTeam2, !team1Winner);

		PMFactory.persistObject(dbMatch);
		MatchServiceHelper.updateTable(matchDto);
		MatchServiceHelper.createDoubleMatchYearAggregation(dbMatch);
		MatchServiceHelper.createDoubleMatchHistories(dbMatch, team1Winner);
		MatchServiceHelper.createTeamMatchYearAggregation(dbMatch);
		MatchServiceHelper.createTeamMatchHistories(dbMatch, team1Winner);

		matchDto.setId(dbMatch.getKey().getId());

		// Lösche paginierte Spieler aus dem Cache
		JCacheFactory.get().remove(JCacheFactory.PAGEDPLAYERS);

		return matchDto;
	}

	/**
	 * Aktualisiert die Einzelspielstatistik.
	 * 
	 * @param dbPlayer Der Spieler.
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param matchPoints Die gewonnene oder verlorene Punktzahl eines Einzelspielers.
	 * @param winner <code>true</code> falls der Spieler gewonnen hat, andernfalls <code>false</code>.
	 */
	private void updatePlayerSingleStats(Player dbPlayer, SingleMatch dbMatch, int matchPoints, boolean winner) {
		final PlayerSingleStats dbPlayerSingleStats = dbPlayer.getPlayerSingleStats();

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
	 * @param matchPoints Die gewonnene oder verlorene Punktzahl eines Doppelspielers.
	 * @param winner <code>true</code> falls der Spieler gewonnen hat, andernfalls <code>false</code>.
	 */
	private void updatePlayerDoubleStats(Player dbPlayer, DoubleMatch dbMatch, int matchPoints, boolean winner) {
		final PlayerDoubleStats dbPlayerDoubleStats = dbPlayer.getPlayerDoubleStats();

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
	 * @param matchPoints Die gewonnene oder verlorene Punktzahl eines Teams.
	 * @param winner <code>true</code> falls das Team gewonnen hat, andernfalls <code>false</code>.
	 */
	private void updateTeamStats(Team dbTeam, DoubleMatch dbMatch, int matchPoints, boolean winner) {
		final TeamStats dbTeamStats = dbTeam.getTeamStats();

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
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<MatchDto> getAllMatchesFrom(Date date) throws KickerLeagueException {
		final List<SingleMatch> dbSingleMatches = MatchBean.getSingleMatchesFrom(date);
		final List<DoubleMatch> dbDoubleMatches = MatchBean.getDoubleMatchesFrom(date);

		final ArrayList<MatchDto> matchDtos = new ArrayList<MatchDto>();
		for (SingleMatch dbMatch : dbSingleMatches) {
			final MatchDto matchDto = MatchServiceHelper.createSingleMatchDto(dbMatch);

			matchDtos.add(matchDto);
		}
		for (DoubleMatch dbMatch : dbDoubleMatches) {
			final MatchDto matchDto = MatchServiceHelper.createDoubleMatchDto(dbMatch);

			matchDtos.add(matchDto);
		}
		Collections.sort(matchDtos, new MatchDescendingComparator());

		return matchDtos;
	}

}
