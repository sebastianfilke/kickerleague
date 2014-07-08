package de.kickerapp.server.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.kickerapp.server.dao.DoubleMatch;
import de.kickerapp.server.dao.Match;
import de.kickerapp.server.dao.MatchHistory;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.PlayerDoubleStats;
import de.kickerapp.server.dao.PlayerSingleStats;
import de.kickerapp.server.dao.SingleMatch;
import de.kickerapp.server.dao.SingleMatchHistory;
import de.kickerapp.server.dao.Stats;
import de.kickerapp.server.dao.Team;
import de.kickerapp.server.dao.TeamStats;
import de.kickerapp.server.dao.fetchplans.PlayerPlan;
import de.kickerapp.server.persistence.PMFactory;
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
	private static final int FACTOR = 10;

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
		matchDto.setMatchDate(dbMatch.getMatchDate());
		matchDto.setMatchType(MatchType.SINGLE);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("'Spiele vom' EEEE, 'den' dd.MM.yyyy", Locale.GERMAN);
		matchDto.setGroupDate(dateFormat.format(dbMatch.getMatchDate()));

		matchDto.setTeam1Dto(new TeamDto(PlayerServiceHelper.createPlayerDto(dbMatch.getPlayer1())));
		matchDto.setTeam2Dto(new TeamDto(PlayerServiceHelper.createPlayerDto(dbMatch.getPlayer2())));

		if (dbMatch.getMatchComment() != null) {
			matchDto.setMatchCommentDto(MatchCommentHelper.createMatchCommentDto(dbMatch.getMatchComment()));
		}

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
		matchDto.setMatchDate(dbMatch.getMatchDate());
		matchDto.setMatchType(MatchType.DOUBLE);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("'Spiele vom' EEEE, 'den' dd.MM.yyyy", Locale.GERMAN);
		matchDto.setGroupDate(dateFormat.format(dbMatch.getMatchDate()));

		matchDto.setTeam1Dto(new TeamDto(PlayerServiceHelper.createPlayerDto(dbMatch.getTeam1().getPlayer1()), PlayerServiceHelper.createPlayerDto(dbMatch
				.getTeam1().getPlayer2())));
		matchDto.setTeam2Dto(new TeamDto(PlayerServiceHelper.createPlayerDto(dbMatch.getTeam2().getPlayer1()), PlayerServiceHelper.createPlayerDto(dbMatch
				.getTeam2().getPlayer2())));

		if (dbMatch.getMatchComment() != null) {
			matchDto.setMatchCommentDto(MatchCommentHelper.createMatchCommentDto(dbMatch.getMatchComment()));
		}

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
	 * @param winner <code>true</code> falls der Spieler gewonnen hat, andernfalls <code>false</code>.
	 * @param dbPlayer Der Spieler.
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
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
	 * @param winner <code>true</code> falls das Team gewonnen hat, andernfalls <code>false</code>.
	 * @param dbTeam Das Team.
	 * @param dbMatch Das Objekt-Datenklassen Spiel.
	 * @return Die gewonnene oder verlorene Punktzahl eines Teams.
	 */
	protected static int getPointsForTeam(boolean winner, Team dbTeam, DoubleMatch dbMatch) {
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
		final double weightedPoints = calculateWeightedPoints(db1Stats, db2Stats);
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
	 * @return Die gewichtete Punktzahl zwischen den Teams bzw. Spielern.
	 */
	private static double calculateWeightedPoints(Stats db1Stats, Stats db2Stats) {
		final double playedGames1 = db1Stats.getWins() + db1Stats.getDefeats();
		final double playedGames2 = db2Stats.getWins() + db2Stats.getDefeats();

		double averageWins1 = 1;
		if (playedGames1 != 0) {
			averageWins1 = (double) db1Stats.getWins() / playedGames1;
		}
		double averageWins2 = 1;
		if (playedGames2 != 0) {
			averageWins2 = (double) db2Stats.getWins() / playedGames2;
		}
		double weightedFactor = 1;
		if (averageWins1 != 0 && averageWins2 != 0) {
			weightedFactor = averageWins2 / ((averageWins1 + averageWins2) / 2);
		}
		return weightedFactor * FACTOR;
	}

	public static void createSingleMatchHistory(SingleMatch dbMatch, boolean team1Winner) {
		final Player dbPlayer1 = PMFactory.getObjectById(Player.class, dbMatch.getPlayer1().getKey().getId(), PlayerPlan.PLAYERSINGLESTATS);
		final Player dbPlayer2 = PMFactory.getObjectById(Player.class, dbMatch.getPlayer2().getKey().getId(), PlayerPlan.PLAYERSINGLESTATS);

		final SingleMatchHistory singleMatchHistoryPlayer1 = createSingleMatchHistory(dbMatch, team1Winner, dbPlayer1, dbPlayer2);
		PMFactory.persistObject(singleMatchHistoryPlayer1);

		final SingleMatchHistory singleMatchHistoryPlayer2 = createSingleMatchHistory(dbMatch, !team1Winner, dbPlayer2, dbPlayer1);
		PMFactory.persistObject(singleMatchHistoryPlayer2);
	}

	private static SingleMatchHistory createSingleMatchHistory(SingleMatch dbMatch, boolean team1Winner, final Player dbPlayer1, final Player dbPlayer2) {
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
		singleMatchHistory.setWinner(team1Winner);
		singleMatchHistory.setPlayer1(dbPlayer1);
		singleMatchHistory.setPlayer2(dbPlayer2);

		return singleMatchHistory;
	}

}
