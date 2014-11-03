package de.kickerapp.server.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.kickerapp.server.dao.DoubleMatch;
import de.kickerapp.server.dao.DoubleMatchHistory;
import de.kickerapp.server.dao.DoubleMatchYearAggregation;
import de.kickerapp.server.dao.Match;
import de.kickerapp.server.dao.MatchHistory;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.PlayerDoubleStats;
import de.kickerapp.server.dao.PlayerSingleStats;
import de.kickerapp.server.dao.SingleMatch;
import de.kickerapp.server.dao.SingleMatchHistory;
import de.kickerapp.server.dao.SingleMatchYearAggregation;
import de.kickerapp.server.dao.Stats;
import de.kickerapp.server.dao.Team;
import de.kickerapp.server.dao.TeamMatchHistory;
import de.kickerapp.server.dao.TeamMatchYearAggregation;
import de.kickerapp.server.dao.TeamStats;
import de.kickerapp.server.dao.fetchplans.PlayerPlan;
import de.kickerapp.server.dao.fetchplans.TeamPlan;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.persistence.queries.MatchBean;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.common.Tendency;
import de.kickerapp.shared.dto.MatchDto;
import de.kickerapp.shared.dto.MatchPointsDto;
import de.kickerapp.shared.dto.MatchSetDto;
import de.kickerapp.shared.dto.TeamDto;

/**
 * Hilfsklasse zur Verarbeitung von Spielen im Klienten.
 * 
 * @author Sebastian Filke
 */
public class MatchServiceHelper {

	/** Der Multiplikator. */
	private static final int FACTOR = 20;

	/**
	 * Comparator zur Sortierung der Team- bzw. Spielerstatistiken.
	 * 
	 * @author Sebastian Filke
	 */
	protected static class StatsComparator implements Comparator<Stats> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(Stats p1, Stats p2) {
			int comp = p2.getPoints().compareTo(p1.getPoints());
			if (comp == 0) {
				comp = p2.getWins().compareTo(p1.getWins());
				if (comp == 0) {
					comp = p2.getDefeats().compareTo(p1.getDefeats());
					if (comp == 0) {
						final Integer goalDifferenceP1 = p1.getShotGoals() - p1.getGetGoals();
						final Integer goalDifferenceP2 = p2.getShotGoals() - p2.getGetGoals();
						comp = goalDifferenceP2.compareTo(goalDifferenceP1);
					}
				}
			}
			return comp;
		}
	}

	/**
	 * Comparator zur absteigenden Sortierung der Spiele.
	 * 
	 * @author Sebastian Filke
	 */
	protected static class MatchDescendingComparator implements Comparator<MatchDto> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(MatchDto m1, MatchDto m2) {
			return m2.getMatchNumber().compareTo(m1.getMatchNumber());
		}
	}

	/**
	 * Comparator zur aufsteigenden Sortierung der Verläufe von Spielen.
	 * 
	 * @author Sebastian Filke
	 */
	public static class MatchHistoryAscendingComparator implements Comparator<MatchHistory> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(MatchHistory m1, MatchHistory m2) {
			return m1.getMatchNumber().compareTo(m2.getMatchNumber());
		}
	}

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbMatch Die Objekt-Datenklasse.
	 * @return Die Client-Datenklasse.
	 */
	protected static MatchDto createSingleMatchDto(SingleMatch dbMatch) {
		final MatchDto matchDto = new MatchDto();
		matchDto.setId(dbMatch.getMatchNumber());
		matchDto.setMatchNumber(dbMatch.getMatchNumber());
		matchDto.setMatchComment(dbMatch.getMatchComment());
		matchDto.setMatchDate(dbMatch.getMatchDate());
		matchDto.setMatchType(MatchType.SINGLE);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("'Spiele vom' EEEE, 'den' dd.MM.yyyy", Locale.GERMAN);
		matchDto.setGroupDate(dateFormat.format(dbMatch.getMatchDate()));

		matchDto.setTeam1Dto(new TeamDto(PlayerServiceHelper.createPlayerDto(dbMatch.getPlayer1())));
		matchDto.setTeam2Dto(new TeamDto(PlayerServiceHelper.createPlayerDto(dbMatch.getPlayer2())));

		final MatchPointsDto pointsDto = new MatchPointsDto();
		pointsDto.setMatchPointsTeam1(dbMatch.getMatchPoints().getMatchPointsTeam1());
		pointsDto.setMatchPointsTeam2(dbMatch.getMatchPoints().getMatchPointsTeam2());
		matchDto.setMatchPointsDto(pointsDto);

		final MatchSetDto setDto = new MatchSetDto();
		setDto.setMatchSetsTeam1(dbMatch.getMatchSets().getMatchSetsTeam1());
		setDto.setMatchSetsTeam2(dbMatch.getMatchSets().getMatchSetsTeam2());
		matchDto.setMatchSetsDto(setDto);

		return matchDto;
	}

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbMatch Die Objekt-Datenklasse.
	 * @return Die Client-Datenklasse.
	 */
	protected static MatchDto createDoubleMatchDto(DoubleMatch dbMatch) {
		final MatchDto matchDto = new MatchDto();
		matchDto.setId(dbMatch.getMatchNumber());
		matchDto.setMatchNumber(dbMatch.getMatchNumber());
		matchDto.setMatchComment(dbMatch.getMatchComment());
		matchDto.setMatchDate(dbMatch.getMatchDate());
		matchDto.setMatchType(MatchType.DOUBLE);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("'Spiele vom' EEEE, 'den' dd.MM.yyyy", Locale.GERMAN);
		matchDto.setGroupDate(dateFormat.format(dbMatch.getMatchDate()));

		matchDto.setTeam1Dto(new TeamDto(PlayerServiceHelper.createPlayerDto(dbMatch.getTeam1().getPlayer1()), PlayerServiceHelper.createPlayerDto(dbMatch
				.getTeam1().getPlayer2())));
		matchDto.setTeam2Dto(new TeamDto(PlayerServiceHelper.createPlayerDto(dbMatch.getTeam2().getPlayer1()), PlayerServiceHelper.createPlayerDto(dbMatch
				.getTeam2().getPlayer2())));

		final MatchPointsDto pointsDto = new MatchPointsDto();
		pointsDto.setMatchPointsTeam1(dbMatch.getMatchPoints().getMatchPointsTeam1());
		pointsDto.setMatchPointsTeam2(dbMatch.getMatchPoints().getMatchPointsTeam2());
		matchDto.setMatchPointsDto(pointsDto);

		final MatchSetDto setDto = new MatchSetDto();
		setDto.setMatchSetsTeam1(dbMatch.getMatchSets().getMatchSetsTeam1());
		setDto.setMatchSetsTeam2(dbMatch.getMatchSets().getMatchSetsTeam2());
		matchDto.setMatchSetsDto(setDto);

		return matchDto;
	}

	/**
	 * Liefert die Information ob das erste Team bzw. Spieler gewonnen hat.
	 * 
	 * @param dbMatch Das Spiel.
	 * @return <code>true</code> falls das erste Team bzw. Spieler gewonnen hat, andernfalls <code>false</code>.
	 */
	protected static boolean isTeam1Winner(Match dbMatch) {
		boolean team1Winner = false;
		int size = 0;
		for (Integer result : dbMatch.getMatchSets().getMatchSetsTeam1()) {
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

	/**
	 * Liefert die geschossenen Tore für das erste Team bzw. Spieler.
	 * 
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @return Die geschossenen Tore für das erste Team bzw. Spieler.
	 */
	protected static int getGoalsTeam1(Match dbMatch) {
		int goals = 0;
		for (Integer result : dbMatch.getMatchSets().getMatchSetsTeam1()) {
			if (result != null) {
				goals = goals + result;
			}
		}
		return goals;
	}

	/**
	 * Liefert die geschossenen Tore für das zweite Team bzw. Spieler.
	 * 
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @return Die geschossenen Tore für das zweite Team bzw. Spieler.
	 */
	protected static int getGoalsTeam2(Match dbMatch) {
		int goals = 0;
		for (Integer result : dbMatch.getMatchSets().getMatchSetsTeam2()) {
			if (result != null) {
				goals = goals + result;
			}
		}
		return goals;
	}

	/**
	 * Liefert die gewonnen Sätze für das erste Team bzw. Spieler.
	 * 
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @return Die gewonnen Sätze für das erste Team bzw. Spieler.
	 */
	protected static int getWinSetsTeam1(Match dbMatch) {
		int winSets = 0;
		for (Integer result : dbMatch.getMatchSets().getMatchSetsTeam1()) {
			if (result != null && result == 6) {
				winSets++;
			}
		}
		return winSets;
	}

	/**
	 * Liefert die gewonnen Sätze für das zweite Team bzw. Spieler.
	 * 
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @return Die gewonnen Sätze für das zweite Team bzw. Spieler.
	 */
	protected static int getWinSetsTeam2(Match dbMatch) {
		int winSets = 0;
		for (Integer result : dbMatch.getMatchSets().getMatchSetsTeam2()) {
			if (result != null && result == 6) {
				winSets++;
			}
		}
		return winSets;
	}

	/**
	 * Aktualisiert die Tabelle bzw. Tabellen.
	 * 
	 * @param matchDto Das Spiel.
	 */
	protected static void updateTable(MatchDto matchDto) {
		if (matchDto.getMatchType() == MatchType.SINGLE) {
			updateSingleStats();
		} else {
			updateDoubleStats();
			updateTeamStats();
		}
	}

	/**
	 * Aktualisiert die Einzelspielstatistiken.
	 */
	private static void updateSingleStats() {
		final List<PlayerSingleStats> dbPlayersSingleStats = PMFactory.getList(PlayerSingleStats.class);
		removeStatsWithZeroMatches(dbPlayersSingleStats);

		Collections.sort(dbPlayersSingleStats, new StatsComparator());
		final int size = dbPlayersSingleStats.size();
		for (int i = 0; i < size; i++) {
			updateStats(i, dbPlayersSingleStats.get(i));
		}
		PMFactory.persistList(dbPlayersSingleStats);
	}

	/**
	 * Aktualisiert die Doppelspielstatistiken.
	 */
	private static void updateDoubleStats() {
		final List<PlayerDoubleStats> dbPlayersDoubleStats = PMFactory.getList(PlayerDoubleStats.class);
		removeStatsWithZeroMatches(dbPlayersDoubleStats);

		Collections.sort(dbPlayersDoubleStats, new StatsComparator());
		final int size = dbPlayersDoubleStats.size();
		for (int i = 0; i < size; i++) {
			updateStats(i, dbPlayersDoubleStats.get(i));
		}
		PMFactory.persistList(dbPlayersDoubleStats);
	}

	/**
	 * Aktualisiert die Teamstatistiken.
	 */
	private static void updateTeamStats() {
		final List<TeamStats> dbTeamStats = PMFactory.getList(TeamStats.class);
		removeStatsWithZeroMatches(dbTeamStats);

		Collections.sort(dbTeamStats, new StatsComparator());
		final int size = dbTeamStats.size();
		for (int i = 0; i < size; i++) {
			updateStats(i, dbTeamStats.get(i));
		}
		PMFactory.persistList(dbTeamStats);
	}

	/**
	 * Entfernt aus der Liste der Statistiken, Statistiken welche noch kein Spiel haben.
	 * 
	 * @param dbStats Die Liste der Statistiken.
	 */
	private static void removeStatsWithZeroMatches(List<? extends Stats> dbStats) {
		final ArrayList<Stats> statsToRemove = new ArrayList<Stats>();
		for (Stats stat : dbStats) {
			if (stat.getWins() == 0 && stat.getDefeats() == 0) {
				statsToRemove.add(stat);
			}
		}
		dbStats.removeAll(statsToRemove);
	}

	/**
	 * Aktualisiert die Statistik des Teams bzw. Spielers.
	 * 
	 * @param i Der aktuelle Tabellenplatz - 1.
	 * @param dbStats Die Statistik des Teams. bzw. Spielers.
	 */
	private static void updateStats(int i, Stats dbStats) {
		dbStats.setPrevTablePlace(dbStats.getCurTablePlace());
		dbStats.setCurTablePlace(i + 1);

		final int preTablePlace = dbStats.getPrevTablePlace();
		final int curTablePlace = dbStats.getCurTablePlace();

		if (preTablePlace == 0) {
			dbStats.setTendency(Tendency.UPWARD);
		} else {
			if (curTablePlace == preTablePlace) {
				dbStats.setTendency(Tendency.CONSTANT);
			} else if (curTablePlace > preTablePlace) {
				dbStats.setTendency(Tendency.DOWNWARD);
			} else if (curTablePlace < preTablePlace) {
				dbStats.setTendency(Tendency.UPWARD);
			}
		}
	}

	/**
	 * Liefert die gewonnene oder verlorene Punktzahl eines Einzelspielers.
	 * 
	 * @param dbPlayer Der Spieler.
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param winner <code>true</code> falls der Spieler gewonnen hat, andernfalls <code>false</code>.
	 * @return Die gewonnene oder verlorene Punktzahl eines Einzelspielers.
	 */
	protected static int getPointsForSinglePlayer(Player dbPlayer, SingleMatch dbMatch, boolean winner) {
		int points = 0;

		final PlayerSingleStats dbCurrentPlayerStats = dbPlayer.getPlayerSingleStats();
		PlayerSingleStats dbPlayer2Stats = null;
		if (dbMatch.getPlayer1() == dbPlayer) {
			dbPlayer2Stats = ((SingleMatch) dbMatch).getPlayer2().getPlayerSingleStats();

			points = getPoints(dbMatch, dbCurrentPlayerStats, dbPlayer2Stats, winner);
		} else if (dbMatch.getPlayer2() == dbPlayer) {
			dbPlayer2Stats = ((SingleMatch) dbMatch).getPlayer1().getPlayerSingleStats();

			points = getPoints(dbMatch, dbCurrentPlayerStats, dbPlayer2Stats, winner);
		}
		return points;
	}

	/**
	 * Liefert die gewonnene oder verlorene Punktzahl eines Doppelspielers.
	 * 
	 * @param dbPlayer Der Spieler.
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param winner <code>true</code> falls der Spieler gewonnen hat, andernfalls <code>false</code>.
	 * @return Die gewonnene oder verlorene Punktzahl eines Doppelspielers.
	 */
	protected static int getPointsForDoublePlayer(Player dbPlayer, DoubleMatch dbMatch, boolean winner) {
		int points = 0;

		int tempPoints1 = 0;
		int tempPoints2 = 0;

		final PlayerDoubleStats dbCurrentPlayerStats = dbPlayer.getPlayerDoubleStats();
		PlayerDoubleStats dbPlayer1Stats = null;
		PlayerDoubleStats dbPlayer2Stats = null;
		if (dbMatch.getTeam1().getPlayer1() == dbPlayer || dbMatch.getTeam1().getPlayer2() == dbPlayer) {
			dbPlayer1Stats = ((DoubleMatch) dbMatch).getTeam2().getPlayer1().getPlayerDoubleStats();
			dbPlayer2Stats = ((DoubleMatch) dbMatch).getTeam2().getPlayer2().getPlayerDoubleStats();

			tempPoints1 = getPoints(dbMatch, dbCurrentPlayerStats, dbPlayer1Stats, winner);
			tempPoints2 = getPoints(dbMatch, dbCurrentPlayerStats, dbPlayer2Stats, winner);
		} else if (dbMatch.getTeam2().getPlayer1() == dbPlayer || dbMatch.getTeam2().getPlayer2() == dbPlayer) {
			dbPlayer1Stats = ((DoubleMatch) dbMatch).getTeam1().getPlayer1().getPlayerDoubleStats();
			dbPlayer2Stats = ((DoubleMatch) dbMatch).getTeam1().getPlayer2().getPlayerDoubleStats();

			tempPoints1 = getPoints(dbMatch, dbCurrentPlayerStats, dbPlayer1Stats, winner);
			tempPoints2 = getPoints(dbMatch, dbCurrentPlayerStats, dbPlayer2Stats, winner);
		}
		points = (int) Math.round((double) (tempPoints1 + tempPoints2) / 2);

		return points;
	}

	/**
	 * Liefert die gewonnene oder verlorene Punktzahl eines Teams.
	 * 
	 * @param dbTeam Das Team.
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param winner <code>true</code> falls das Team gewonnen hat, andernfalls <code>false</code>.
	 * @return Die gewonnene oder verlorene Punktzahl eines Teams.
	 */
	protected static int getPointsForTeam(Team dbTeam, DoubleMatch dbMatch, boolean winner) {
		int points = 0;
		int tempPoints = 0;

		final TeamStats dbCurrentTeamStats = (TeamStats) dbTeam.getTeamStats();
		TeamStats dbTeam2Stats = null;
		if (dbMatch.getTeam1() == dbTeam) {
			dbTeam2Stats = ((DoubleMatch) dbMatch).getTeam2().getTeamStats();

			tempPoints = getPoints(dbMatch, dbCurrentTeamStats, dbTeam2Stats, winner);
		} else if (dbMatch.getTeam2() == dbTeam) {
			dbTeam2Stats = ((DoubleMatch) dbMatch).getTeam1().getTeamStats();

			tempPoints = getPoints(dbMatch, dbCurrentTeamStats, dbTeam2Stats, winner);
		}
		points = tempPoints;

		return points;
	}

	/**
	 * Liefert die gewonnene oder verlorene Punktzahl eines Teams bzw. Spielers.
	 * 
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param db1Stats Die Teamspiel-, Doppelspiel- oder Einzelspiel-Statistik des ersten Teams bzw. Spielers.
	 * @param db2Stats Die Teamspiel-, Doppelspiel- oder Einzelspiel-Statistik des zweiten Teams bzw. Spielers.
	 * @param winner <code>true</code> falls das Team bzw. Spieler gewonnen hat, andernfalls <code>false</code>.
	 * @return Die gewonnene oder verlorene Punktzahl eines Teams bzw Spielers.
	 */
	private static int getPoints(Match dbMatch, Stats db1Stats, Stats db2Stats, boolean winner) {
		final double weightedPoints = calculateWeightedPoints(db1Stats, db2Stats, winner);
		final int matchSets = dbMatch.getMatchSets().getMatchSetsTeam1().size();

		int multiplicatorPoints = (int) weightedPoints;
		if (winner && matchSets == 2) {
			multiplicatorPoints = (int) (weightedPoints * 1.5);
		} else if (!winner && matchSets == 3) {
			multiplicatorPoints = (int) (weightedPoints * 0.5);
		}
		if (!winner) {
			multiplicatorPoints = multiplicatorPoints * -1;
		}
		return multiplicatorPoints;
	}

	/**
	 * Liefert die gewichtete Punktzahl eines Teams bzw. Spielers.
	 * 
	 * @param db1Stats Die Teamspiel-, Doppelspiel- oder Einzelspiel-Statistik des ersten Teams bzw. Spielers.
	 * @param db2Stats Die Teamspiel-, Doppelspiel- oder Einzelspiel-Statistik des zweiten Teams bzw. Spielers.
	 * @param winner <code>true</code> falls das Team bzw. Spieler gewonnen hat, andernfalls <code>false</code>.
	 * @return Die gewichtete Punktzahl zwischen den Teams bzw. Spielern.
	 */
	private static double calculateWeightedPoints(Stats db1Stats, Stats db2Stats, boolean winner) {
		final double winsPlayers1 = db1Stats.getWins();
		final double defeatsPlayers1 = db1Stats.getDefeats();

		final double winsPlayers2 = db2Stats.getWins();
		final double defeatsPlayers2 = db2Stats.getDefeats();

		double oddsPlayer1;
		oddsPlayer1 = (winsPlayers1 * defeatsPlayers2) / (winsPlayers2 * defeatsPlayers1);
		if (oddsPlayer1 == 0 || Double.isNaN(oddsPlayer1) || Double.isInfinite(oddsPlayer1)) {
			oddsPlayer1 = 1;
		}

		double oddsPlayer2 = (winsPlayers2 * defeatsPlayers1) / (winsPlayers1 * defeatsPlayers2);
		if (oddsPlayer2 == 0 || Double.isNaN(oddsPlayer2) || Double.isInfinite(oddsPlayer2)) {
			oddsPlayer2 = 1;
		}

		double oddsFactor;
		if (winner) {
			if (oddsPlayer1 - oddsPlayer2 <= 0) {
				oddsFactor = oddsPlayer2 / (oddsPlayer2 + oddsPlayer1);
			} else {
				oddsFactor = oddsPlayer1 / (oddsPlayer2 + oddsPlayer1);
			}
		} else {
			if (oddsPlayer1 - oddsPlayer2 <= 0) {
				oddsFactor = oddsPlayer2 / (oddsPlayer2 + oddsPlayer1);
			} else {
				oddsFactor = oddsPlayer1 / (oddsPlayer2 + oddsPlayer1);
			}
		}
		return oddsFactor * FACTOR;
	}

	/**
	 * Erstellt die Informationen für den Verlauf des Einzelspiels.
	 * 
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param team1Winner <code>true</code> falls der erste Spieler gewonnen hat, andernfalls <code>false</code>.
	 */
	protected static void createSingleMatchHistories(SingleMatch dbMatch, boolean team1Winner) {
		final Player dbPlayer1 = PMFactory.getObjectById(Player.class, dbMatch.getPlayer1().getKey().getId(), PlayerPlan.PLAYERSINGLESTATS);
		final Player dbPlayer2 = PMFactory.getObjectById(Player.class, dbMatch.getPlayer2().getKey().getId(), PlayerPlan.PLAYERSINGLESTATS);

		final SingleMatchHistory singleMatchHistoryPlayer1 = createSingleMatchHistory(dbMatch, team1Winner, dbPlayer1, dbPlayer2);
		final SingleMatchHistory singleMatchHistoryPlayer2 = createSingleMatchHistory(dbMatch, !team1Winner, dbPlayer2, dbPlayer1);

		PMFactory.persistAllObjects(singleMatchHistoryPlayer1, singleMatchHistoryPlayer2);
	}

	/**
	 * Erstellt die Information für den Verlauf eines Einzelspiels.
	 * 
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param winner <code>true</code> falls der Spieler gewonnen hat, andernfalls <code>false</code>.
	 * @param dbPlayer1 Der erste Spieler des Einzelspiels.
	 * @param dbPlayer2 Der zweite Spieler des Einzelspiels.
	 * @return Die erzeugte Information für den Verlauf eines Einzelspiels.
	 */
	private static SingleMatchHistory createSingleMatchHistory(SingleMatch dbMatch, boolean winner, final Player dbPlayer1, final Player dbPlayer2) {
		final SingleMatchHistory singleMatchHistory = new SingleMatchHistory();
		final int matchHistoryId = PMFactory.getNextId(SingleMatchHistory.class.getName());
		final Key matchHistoryKey = KeyFactory.createKey(SingleMatchHistory.class.getSimpleName(), matchHistoryId);
		singleMatchHistory.setKey(matchHistoryKey);
		singleMatchHistory.setMatchNumber(dbMatch.getMatchNumber());
		singleMatchHistory.setMatchDate(dbMatch.getMatchDate());
		singleMatchHistory.setTotalPoints(dbPlayer1.getPlayerSingleStats().getPoints());

		if (dbMatch.getPlayer1().getKey().getId() == dbPlayer1.getKey().getId()) {
			singleMatchHistory.setMatchPoints(dbMatch.getMatchPoints().getMatchPointsTeam1().get(0));
			singleMatchHistory.setShotGoals(getGoalsTeam1(dbMatch));
			singleMatchHistory.setGetGoals(getGoalsTeam2(dbMatch));
		} else if (dbMatch.getPlayer2().getKey().getId() == dbPlayer1.getKey().getId()) {
			singleMatchHistory.setMatchPoints(dbMatch.getMatchPoints().getMatchPointsTeam2().get(0));
			singleMatchHistory.setShotGoals(getGoalsTeam2(dbMatch));
			singleMatchHistory.setGetGoals(getGoalsTeam1(dbMatch));
		}
		singleMatchHistory.setTablePlace(dbPlayer1.getPlayerSingleStats().getCurTablePlace());
		singleMatchHistory.setWinner(winner);
		singleMatchHistory.setPlayer1(dbPlayer1);
		singleMatchHistory.setPlayer2(dbPlayer2);

		return singleMatchHistory;
	}

	/**
	 * Erstellt die Informationen für die Anzahl der Einzelspiele eines Spielers.
	 * 
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 */
	protected static void createSingleMatchYearAggregation(SingleMatch dbMatch) {
		final Player dbPlayer1 = PMFactory.getObjectById(Player.class, dbMatch.getPlayer1().getKey().getId(), PlayerPlan.PLAYERSINGLESTATS);
		final Player dbPlayer2 = PMFactory.getObjectById(Player.class, dbMatch.getPlayer2().getKey().getId(), PlayerPlan.PLAYERSINGLESTATS);
		final int year = getYearForMatch(dbMatch);

		final SingleMatchYearAggregation singleMatchYearAggregationPlayer1 = MatchBean.getSingleMatchYearAggregationForPlayer(dbPlayer1, year);
		final int sumMatchesPlayer1 = singleMatchYearAggregationPlayer1.getSumMatches() + 1;
		singleMatchYearAggregationPlayer1.setSumMatches(sumMatchesPlayer1);

		final SingleMatchYearAggregation singleMatchYearAggregationPlayer2 = MatchBean.getSingleMatchYearAggregationForPlayer(dbPlayer2, year);
		final int sumMatchesPlayer2 = singleMatchYearAggregationPlayer2.getSumMatches() + 1;
		singleMatchYearAggregationPlayer2.setSumMatches(sumMatchesPlayer2);

		PMFactory.persistAllObjects(singleMatchYearAggregationPlayer1, singleMatchYearAggregationPlayer2);
	}

	/**
	 * Erstellt die Informationen für den Verlauf des Doppelspiels.
	 * 
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param team1Winner <code>true</code> falls das erste Team gewonnen hat, andernfalls <code>false</code>.
	 */
	protected static void createDoubleMatchHistories(DoubleMatch dbMatch, boolean team1Winner) {
		final Player dbPlayer1 = PMFactory.getObjectById(Player.class, dbMatch.getTeam1().getPlayer1().getKey().getId(), PlayerPlan.PLAYERDOUBLESTATS);
		final Player dbPlayer2 = PMFactory.getObjectById(Player.class, dbMatch.getTeam1().getPlayer2().getKey().getId(), PlayerPlan.PLAYERDOUBLESTATS);
		final Player dbPlayer3 = PMFactory.getObjectById(Player.class, dbMatch.getTeam2().getPlayer1().getKey().getId(), PlayerPlan.PLAYERDOUBLESTATS);
		final Player dbPlayer4 = PMFactory.getObjectById(Player.class, dbMatch.getTeam2().getPlayer2().getKey().getId(), PlayerPlan.PLAYERDOUBLESTATS);

		final DoubleMatchHistory doubleMatchHistoryPlayer1 = createDoubleMatchHistory(dbMatch, team1Winner, dbPlayer1, dbPlayer2, dbPlayer3, dbPlayer4);
		final DoubleMatchHistory doubleMatchHistoryPlayer2 = createDoubleMatchHistory(dbMatch, team1Winner, dbPlayer2, dbPlayer1, dbPlayer3, dbPlayer4);
		final DoubleMatchHistory doubleMatchHistoryPlayer3 = createDoubleMatchHistory(dbMatch, !team1Winner, dbPlayer3, dbPlayer4, dbPlayer1, dbPlayer2);
		final DoubleMatchHistory doubleMatchHistoryPlayer4 = createDoubleMatchHistory(dbMatch, !team1Winner, dbPlayer4, dbPlayer3, dbPlayer1, dbPlayer2);

		PMFactory.persistAllObjects(doubleMatchHistoryPlayer1, doubleMatchHistoryPlayer2, doubleMatchHistoryPlayer3, doubleMatchHistoryPlayer4);
	}

	/**
	 * Erstellt die Information für den Verlauf eines Doppelspiels.
	 * 
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param winner <code>true</code> falls der Spieler gewonnen hat, andernfalls <code>false</code>.
	 * @param dbPlayer1 Der erste Spieler des Doppelspiels.
	 * @param dbPlayer2 Der zweite Spieler des Doppelspiels.
	 * @param dbPlayer3 Der dritte Spieler des Doppelspiels.
	 * @param dbPlayer4 Der vierte Spieler des Doppelspiels.
	 * @return Die erzeugte Information für den Verlauf eines Doppelspiels.
	 */
	private static DoubleMatchHistory createDoubleMatchHistory(DoubleMatch dbMatch, boolean winner, final Player dbPlayer1, final Player dbPlayer2,
			final Player dbPlayer3, final Player dbPlayer4) {
		final DoubleMatchHistory doubleMatchHistory = new DoubleMatchHistory();
		final int matchHistoryId = PMFactory.getNextId(DoubleMatchHistory.class.getName());
		final Key matchHistoryKey = KeyFactory.createKey(DoubleMatchHistory.class.getSimpleName(), matchHistoryId);
		doubleMatchHistory.setKey(matchHistoryKey);
		doubleMatchHistory.setMatchNumber(dbMatch.getMatchNumber());
		doubleMatchHistory.setMatchDate(dbMatch.getMatchDate());
		doubleMatchHistory.setTotalPoints(dbPlayer1.getPlayerDoubleStats().getPoints());

		if (dbMatch.getTeam1().getPlayer1().getKey().getId() == dbPlayer1.getKey().getId()
				|| dbMatch.getTeam1().getPlayer2().getKey().getId() == dbPlayer1.getKey().getId()) {
			doubleMatchHistory.setMatchPoints(dbMatch.getMatchPoints().getMatchPointsTeam1().get(0));
			doubleMatchHistory.setShotGoals(getGoalsTeam1(dbMatch));
			doubleMatchHistory.setGetGoals(getGoalsTeam2(dbMatch));
		} else if (dbMatch.getTeam2().getPlayer1().getKey().getId() == dbPlayer1.getKey().getId()
				|| dbMatch.getTeam2().getPlayer2().getKey().getId() == dbPlayer1.getKey().getId()) {
			doubleMatchHistory.setMatchPoints(dbMatch.getMatchPoints().getMatchPointsTeam2().get(0));
			doubleMatchHistory.setShotGoals(getGoalsTeam2(dbMatch));
			doubleMatchHistory.setGetGoals(getGoalsTeam1(dbMatch));
		}
		doubleMatchHistory.setTablePlace(dbPlayer1.getPlayerDoubleStats().getCurTablePlace());
		doubleMatchHistory.setWinner(winner);
		doubleMatchHistory.setPlayer1(dbPlayer1);
		doubleMatchHistory.setPlayer2(dbPlayer2);
		doubleMatchHistory.setPlayer3(dbPlayer3);
		doubleMatchHistory.setPlayer4(dbPlayer4);

		return doubleMatchHistory;
	}

	/**
	 * Erstellt die Informationen für die Anzahl der Einzelspiele eines Spielers.
	 * 
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 */
	protected static void createDoubleMatchYearAggregation(DoubleMatch dbMatch) {
		final Team dbTeam1 = PMFactory.getObjectById(Team.class, dbMatch.getTeam1().getKey().getId(), TeamPlan.BOTHPLAYERS);
		final Team dbTeam2 = PMFactory.getObjectById(Team.class, dbMatch.getTeam2().getKey().getId(), TeamPlan.BOTHPLAYERS);
		final int year = getYearForMatch(dbMatch);

		final DoubleMatchYearAggregation doubleMatchYearAggregationTeam1Player1 = MatchBean.getDoubleMatchYearAggregationForPlayer(dbTeam1.getPlayer1(), year);
		final int sumMatchesTeam1Player1 = doubleMatchYearAggregationTeam1Player1.getSumMatches() + 1;
		doubleMatchYearAggregationTeam1Player1.setSumMatches(sumMatchesTeam1Player1);

		final DoubleMatchYearAggregation doubleMatchYearAggregationTeam1Player2 = MatchBean.getDoubleMatchYearAggregationForPlayer(dbTeam1.getPlayer2(), year);
		final int sumMatchesTeam1Player2 = doubleMatchYearAggregationTeam1Player2.getSumMatches() + 1;
		doubleMatchYearAggregationTeam1Player2.setSumMatches(sumMatchesTeam1Player2);

		final DoubleMatchYearAggregation doubleMatchYearAggregationTeam2Player1 = MatchBean.getDoubleMatchYearAggregationForPlayer(dbTeam2.getPlayer1(), year);
		final int sumMatchesTeam2Player1 = doubleMatchYearAggregationTeam2Player1.getSumMatches() + 1;
		doubleMatchYearAggregationTeam2Player1.setSumMatches(sumMatchesTeam2Player1);

		final DoubleMatchYearAggregation doubleMatchYearAggregationTeam2Player2 = MatchBean.getDoubleMatchYearAggregationForPlayer(dbTeam2.getPlayer2(), year);
		final int sumMatchesTeam2Player2 = doubleMatchYearAggregationTeam2Player2.getSumMatches() + 1;
		doubleMatchYearAggregationTeam2Player2.setSumMatches(sumMatchesTeam2Player2);

		PMFactory.persistAllObjects(doubleMatchYearAggregationTeam1Player1, doubleMatchYearAggregationTeam1Player2, doubleMatchYearAggregationTeam2Player1,
				doubleMatchYearAggregationTeam2Player2);
	}

	/**
	 * Erstellt die Informationen für den Verlauf des Teamspiels.
	 * 
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param team1Winner <code>true</code> falls das erste Team gewonnen hat, andernfalls <code>false</code>.
	 */
	protected static void createTeamMatchHistories(DoubleMatch dbMatch, boolean team1Winner) {
		final Team dbTeam1 = PMFactory.getObjectById(Team.class, dbMatch.getTeam1().getKey().getId(), TeamPlan.TEAMSTATS, TeamPlan.BOTHPLAYERS);
		final Team dbTeam2 = PMFactory.getObjectById(Team.class, dbMatch.getTeam1().getKey().getId(), TeamPlan.TEAMSTATS, TeamPlan.BOTHPLAYERS);

		final TeamMatchHistory teamMatchHistoryTeam1 = createTeamMatchHistory(dbMatch, team1Winner, dbTeam1, dbTeam2);
		final TeamMatchHistory teamMatchHistoryTeam2 = createTeamMatchHistory(dbMatch, !team1Winner, dbTeam2, dbTeam1);

		PMFactory.persistAllObjects(teamMatchHistoryTeam1, teamMatchHistoryTeam2);
	}

	/**
	 * Erstellt die Information für den Verlauf eines Teamspiels.
	 * 
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @param team1Winner <code>true</code> falls das erste Team gewonnen hat, andernfalls <code>false</code>.
	 * @param dbTeam1 Das erste Team des Doppelspiels.
	 * @param dbTeam2 Das zweite Team des Doppelspiels.
	 * @return Die erzeugte Information für den Verlauf eines Teamspiels.
	 */
	private static TeamMatchHistory createTeamMatchHistory(DoubleMatch dbMatch, boolean team1Winner, final Team dbTeam1, final Team dbTeam2) {
		final TeamMatchHistory teamMatchHistory = new TeamMatchHistory();
		final int matchHistoryId = PMFactory.getNextId(TeamMatchHistory.class.getName());
		final Key matchHistoryKey = KeyFactory.createKey(TeamMatchHistory.class.getSimpleName(), matchHistoryId);
		teamMatchHistory.setKey(matchHistoryKey);
		teamMatchHistory.setMatchNumber(dbMatch.getMatchNumber());
		teamMatchHistory.setMatchDate(dbMatch.getMatchDate());
		teamMatchHistory.setTotalPoints(dbTeam1.getTeamStats().getPoints());

		if (dbMatch.getTeam1().getKey().getId() == dbTeam1.getKey().getId()) {
			teamMatchHistory.setMatchPoints(dbMatch.getMatchPoints().getMatchPointsTeam1().get(0));
			teamMatchHistory.setShotGoals(getGoalsTeam1(dbMatch));
			teamMatchHistory.setGetGoals(getGoalsTeam2(dbMatch));
		} else if (dbMatch.getTeam2().getKey().getId() == dbTeam1.getKey().getId()) {
			teamMatchHistory.setMatchPoints(dbMatch.getMatchPoints().getMatchPointsTeam2().get(0));
			teamMatchHistory.setShotGoals(getGoalsTeam2(dbMatch));
			teamMatchHistory.setGetGoals(getGoalsTeam1(dbMatch));
		}
		teamMatchHistory.setTablePlace(dbTeam1.getTeamStats().getCurTablePlace());
		teamMatchHistory.setWinner(team1Winner);
		teamMatchHistory.setTeam1(dbTeam1);
		teamMatchHistory.setTeam2(dbTeam2);

		return teamMatchHistory;
	}

	/**
	 * Erstellt die Informationen für die Anzahl der Einzelspiele eines Spielers.
	 * 
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 */
	protected static void createTeamMatchYearAggregation(DoubleMatch dbMatch) {
		final Team dbTeam1 = PMFactory.getObjectById(Team.class, dbMatch.getTeam1().getKey().getId(), TeamPlan.BOTHPLAYERS);
		final Team dbTeam2 = PMFactory.getObjectById(Team.class, dbMatch.getTeam2().getKey().getId(), TeamPlan.BOTHPLAYERS);
		final int year = getYearForMatch(dbMatch);

		final TeamMatchYearAggregation teamMatchYearAggregationTeam1 = MatchBean.getTeamMatchYearAggregationForTeam(dbTeam1, year);
		final int sumMatchesTeam1 = teamMatchYearAggregationTeam1.getSumMatches() + 1;
		teamMatchYearAggregationTeam1.setSumMatches(sumMatchesTeam1);

		final TeamMatchYearAggregation teamMatchYearAggregationPlayer2 = MatchBean.getTeamMatchYearAggregationForTeam(dbTeam2, year);
		final int sumMatchesTeam2 = teamMatchYearAggregationPlayer2.getSumMatches() + 1;
		teamMatchYearAggregationPlayer2.setSumMatches(sumMatchesTeam2);

		PMFactory.persistAllObjects(teamMatchYearAggregationTeam1, teamMatchYearAggregationPlayer2);
	}

	/**
	 * Liefert das Jahr des Spieles.
	 * 
	 * @param dbMatch Das Spiel.
	 * @return Das Jahr des Spieles.
	 */
	private static int getYearForMatch(Match dbMatch) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(dbMatch.getMatchDate());

		return calendar.get(Calendar.YEAR);
	}

}
