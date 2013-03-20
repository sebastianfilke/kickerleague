package de.kickerapp.server.services;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import de.kickerapp.server.entity.Match;
import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.PlayerDoubleStats;
import de.kickerapp.server.entity.PlayerSingleStats;
import de.kickerapp.server.entity.Stats;
import de.kickerapp.server.entity.Team;
import de.kickerapp.server.entity.TeamStats;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.common.Tendency;
import de.kickerapp.shared.dto.MatchDto;
import de.kickerapp.shared.dto.MatchPointsDto;
import de.kickerapp.shared.dto.MatchSetDto;
import de.kickerapp.shared.dto.TeamDto;

/**
 * Hilfsklasse zur Verarbeitung von Spielen im Clienten.
 * 
 * @author Sebastian Filke
 */
public class MatchServiceHelper {

	/** Die Punkte, falls es sich um ein Spiel mit 2 Sätzen handelt und das erste Team bzw. Spieler mehr Punkte hat. */
	private static final int[][] POINTS_A = new int[][] { { 20, -18 }, { 17, -14 }, { 14, -10 }, { 11, -8 }, { 9, -6 }, { 7, -4 }, { 5, -3 }, { 4, -2 } };
	/** Die Punkte, falls es sich um ein Spiel mit 3 Sätzen handelt und das erste Team bzw. Spieler mehr Punkte hat. */
	private static final int[][] POINTS_B = new int[][] { { 17, -15 }, { 14, -11 }, { 11, -8 }, { 9, -6 }, { 7, -4 }, { 5, -3 }, { 3, -2 }, { 2, -1 } };
	/** Die Punkte, falls es sich um ein Spiel mit 3 Sätzen handelt und das erste Team bzw. Spieler weniger Punkte hat. */
	private static final int[][] POINTS_C = new int[][] { { 19, -16 }, { 21, -18 }, { 23, -20 }, { 26, -23 }, { 28, -25 }, { 30, -27 }, { 32, -29 },
			{ 34, -31 } };
	/** Die Punkte, falls es sich um ein Spiel mit 2 Sätzen handelt und das erste Team bzw. Spieler weniger Punkte hat. */
	private static final int[][] POINTS_D = new int[][] { { 22, -19 }, { 24, -21 }, { 25, -23 }, { 28, -25 }, { 30, -27 }, { 32, -30 }, { 35, -32 },
			{ 38, -34 } };

	/**
	 * Comparator zur Sortierung der Team- bzw. Spielerstatistiken.
	 * 
	 * @author Sebastian Filke
	 */
	protected static class StatsComparator implements Comparator<Stats>, Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = -8784066470788748810L;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(Stats p1, Stats p2) {
			int comp = p2.getPoints().compareTo(p1.getPoints());
			if (comp == 0) {
				comp = p2.getWins().compareTo(p1.getWins());
				if (comp == 0) {
					comp = p2.getLosses().compareTo(p1.getLosses());
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
	 * Comparator zur Sortierung der Spiele.
	 * 
	 * @author Sebastian Filke
	 */
	protected static class MatchComparator implements Comparator<Match>, Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = 3150940865430023409L;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(Match m1, Match m2) {
			return m2.getMatchNumber().compareTo(m1.getMatchNumber());
		}
	}

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbMatch Die Objekt-Datenklasse.
	 * @param dbPlayers Die Objekt-Datenklasse-Liste aller Spieler.
	 * @param dbTeams Die Objekt-Datenklasse-Liste aller Teams.
	 * @return Die Client-Datenklasse.
	 */
	protected static MatchDto createDtoMatch(Match dbMatch, List<Player> dbPlayers, List<Team> dbTeams) {
		final MatchDto matchDto = new MatchDto();
		matchDto.setId(dbMatch.getKey().getId());
		matchDto.setMatchNumber(dbMatch.getMatchNumber());
		matchDto.setMatchDate(dbMatch.getMatchDate());
		matchDto.setMatchType(dbMatch.getMatchType());

		final SimpleDateFormat dateFormat = new SimpleDateFormat("'Spiele vom' EEEE, 'den' dd.MM.yyyy", Locale.GERMAN);
		matchDto.setGroupDate(dateFormat.format(dbMatch.getMatchDate()));

		if (dbMatch.getMatchType() == MatchType.SINGLE) {
			final Player team1Player1 = PlayerServiceHelper.getPlayerById(dbMatch.getTeam1(), dbPlayers);
			matchDto.setTeam1Dto(new TeamDto(PlayerServiceHelper.createPlayerDto(team1Player1)));

			final Player team2Player2 = PlayerServiceHelper.getPlayerById(dbMatch.getTeam2(), dbPlayers);
			matchDto.setTeam2(new TeamDto(PlayerServiceHelper.createPlayerDto(team2Player2)));
		} else {
			final Team team1 = TeamServiceHelper.getTeamById(dbMatch.getTeam1(), dbTeams);

			final Player team1Player1 = PlayerServiceHelper.getPlayerById((Long) team1.getPlayers().toArray()[0], dbPlayers);
			final Player team1Player2 = PlayerServiceHelper.getPlayerById((Long) team1.getPlayers().toArray()[1], dbPlayers);

			matchDto.setTeam1Dto(new TeamDto(PlayerServiceHelper.createPlayerDto(team1Player1), PlayerServiceHelper.createPlayerDto(team1Player2)));

			final Team team2 = TeamServiceHelper.getTeamById(dbMatch.getTeam2(), dbTeams);

			final Player team2Player1 = PlayerServiceHelper.getPlayerById((Long) team2.getPlayers().toArray()[0], dbPlayers);
			final Player team2Player2 = PlayerServiceHelper.getPlayerById((Long) team2.getPlayers().toArray()[1], dbPlayers);

			matchDto.setTeam2(new TeamDto(PlayerServiceHelper.createPlayerDto(team2Player1), PlayerServiceHelper.createPlayerDto(team2Player2)));
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
	 * @param matchDto Das Spiel.
	 * @return <code>true</code> falls das erste Team bzw. Spieler gewonnen hat, andernfalls <code>false</code>.
	 */
	protected static boolean isTeam1Winner(MatchDto matchDto) {
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

	/**
	 * Liefert die geschossenen Tore für das erste Team bzw. Spieler.
	 * 
	 * @param matchDto Das Spiel.
	 * @return Die geschossenen Tore für das erste Team bzw. Spieler.
	 */
	protected static int getGoalsTeam1(MatchDto matchDto) {
		int goals = 0;
		for (Integer result : matchDto.getMatchSetsDto().getMatchSetsTeam1()) {
			if (result != null) {
				goals = goals + result;
			}
		}
		return goals;
	}

	/**
	 * Liefert die geschossenen Tore für das zweite Team bzw. Spieler.
	 * 
	 * @param matchDto Das Spiel.
	 * @return Die geschossenen Tore für das zweite Team bzw. Spieler.
	 */
	protected static int getGoalsTeam2(MatchDto matchDto) {
		int goals = 0;
		for (Integer result : matchDto.getMatchSetsDto().getMatchSetsTeam2()) {
			if (result != null) {
				goals = goals + result;
			}
		}
		return goals;
	}

	/**
	 * Liefert den Spieler anhand der Db-Id.
	 * 
	 * @param dbTeam1Player1 Der erste Spieler.
	 * @param dbTeam1Player2 Der zweite Spieler.
	 * @param id Die Datenbank-Id des zu suchenden Spielers.
	 * @return Der Spieler.
	 */
	protected static Player getPlayerForTeamId(Player dbTeam1Player1, Player dbTeam1Player2, Long id) {
		Player player = null;
		if (dbTeam1Player1.getKey().getId() == id) {
			player = dbTeam1Player1;
		} else if (dbTeam1Player2.getKey().getId() == id) {
			player = dbTeam1Player2;
		}
		return player;
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
	 * @param dbStats Die Liste der statistiken.
	 */
	private static void removeStatsWithZeroMatches(List<? extends Stats> dbStats) {
		final ArrayList<Stats> statsToRemove = new ArrayList<Stats>();
		for (Stats stat : dbStats) {
			if (stat.getWins() == 0 && stat.getLosses() == 0) {
				statsToRemove.add(stat);
			}
		}
		dbStats.removeAll(statsToRemove);
	}

	/**
	 * Aktualisiert die Statistik des Teams bzw. Spielers.
	 * 
	 * @param i Der aktuelle Tabelleplatz - 1.
	 * @param dbStats Die Statistik des Teams. bzw. Spielers.
	 */
	private static void updateStats(int i, Stats dbStats) {
		dbStats.setPrevTablePlace(dbStats.getCurTablePlace());
		dbStats.setCurTablePlace(i + 1);

		final int preTablePlace = dbStats.getPrevTablePlace();
		final int curTablePlace = dbStats.getCurTablePlace();

		if (preTablePlace == 0) {
			dbStats.setTendency(Tendency.Upward);
		} else {
			if (curTablePlace == preTablePlace) {
				dbStats.setTendency(Tendency.Constant);
			} else if (curTablePlace > preTablePlace) {
				dbStats.setTendency(Tendency.Downward);
			} else if (curTablePlace < preTablePlace) {
				dbStats.setTendency(Tendency.Upward);
			}
		}
	}

	/**
	 * Liefert die gewonnene oder verlorene Punktzahl eines Spielers.
	 * 
	 * @param winner <code>true</code> falls der Spieler gewonnen hat, andernfalls <code>false</code>.
	 * @param dbPlayer Der Spieler.
	 * @param dbPlayerStats Die Einzelspiel- bzw. Doppelspiel-Statistik des Spielers.
	 * @param matchDto Das Spiel.
	 * @return Die gewonnene oder verlorene Punktzahl eines Spielers.
	 */
	protected static int getPointsForPlayer(boolean winner, Player dbPlayer, Stats dbPlayerStats, MatchDto matchDto) {
		int points = 0;
		if (matchDto.getMatchType() == MatchType.SINGLE) {
			int tempPoints = 0;

			final PlayerSingleStats dbCurrentPlayerStats = (PlayerSingleStats) dbPlayerStats;
			PlayerSingleStats dbPlayer2Stats = null;
			if (matchDto.getTeam1Dto().getPlayer1().getId() == dbPlayer.getKey().getId()) {
				dbPlayer2Stats = PMFactory.getObjectById(PlayerSingleStats.class, matchDto.getTeam2Dto().getPlayer1().getId());

				tempPoints = getPoints(winner, matchDto, dbCurrentPlayerStats, dbPlayer2Stats);
			} else {
				dbPlayer2Stats = PMFactory.getObjectById(PlayerSingleStats.class, matchDto.getTeam1Dto().getPlayer1().getId());

				tempPoints = getPoints(winner, matchDto, dbCurrentPlayerStats, dbPlayer2Stats);
			}

			points = tempPoints;
		} else {
			int tempPoints1 = 0;
			int tempPoints2 = 0;

			final PlayerDoubleStats dbCurrentPlayerStats = (PlayerDoubleStats) dbPlayerStats;
			PlayerDoubleStats dbPlayer1Stats = null;
			PlayerDoubleStats dbPlayer2Stats = null;
			if (matchDto.getTeam1Dto().getPlayer1().getId() == dbPlayer.getKey().getId()
					|| matchDto.getTeam1Dto().getPlayer2().getId() == dbPlayer.getKey().getId()) {
				dbPlayer1Stats = PMFactory.getObjectById(PlayerDoubleStats.class, matchDto.getTeam2Dto().getPlayer1().getId());
				dbPlayer2Stats = PMFactory.getObjectById(PlayerDoubleStats.class, matchDto.getTeam2Dto().getPlayer2().getId());

				tempPoints1 = getPoints(winner, matchDto, dbCurrentPlayerStats, dbPlayer1Stats);
				tempPoints2 = getPoints(winner, matchDto, dbCurrentPlayerStats, dbPlayer2Stats);
			} else {
				dbPlayer1Stats = PMFactory.getObjectById(PlayerDoubleStats.class, matchDto.getTeam1Dto().getPlayer1().getId());
				dbPlayer2Stats = PMFactory.getObjectById(PlayerDoubleStats.class, matchDto.getTeam1Dto().getPlayer2().getId());

				tempPoints1 = getPoints(winner, matchDto, dbCurrentPlayerStats, dbPlayer1Stats);
				tempPoints2 = getPoints(winner, matchDto, dbCurrentPlayerStats, dbPlayer2Stats);
			}

			points = Math.round((float) (tempPoints1 + tempPoints2) / 2);
		}
		return points;
	}

	/**
	 * Liefert die gewonnene oder verlorene Punktzahl eines Teams.
	 * 
	 * @param winner <code>true</code> falls das Team gewonnen hat, andernfalls <code>false</code>.
	 * @param dbTeam Das Team.
	 * @param dbTeamStats Die Teamspiel-Statistik des Spielers.
	 * @param matchDto Das Spiel.
	 * @return Die gewonnene oder verlorene Punktzahl eines Teams.
	 */
	protected static int getPointsForTeam(boolean winner, Team dbTeam, Stats dbTeamStats, MatchDto matchDto) {
		int points = 0;
		int tempPoints = 0;

		final TeamStats dbCurrentTeamStats = (TeamStats) dbTeamStats;
		TeamStats dbTeam2Stats = null;
		if (matchDto.getTeam1Dto().getId() == dbTeam.getKey().getId()) {
			dbTeam2Stats = PMFactory.getObjectById(TeamStats.class, matchDto.getTeam2Dto().getId());

			tempPoints = getPoints(winner, matchDto, dbCurrentTeamStats, dbTeam2Stats);
		} else {
			dbTeam2Stats = PMFactory.getObjectById(TeamStats.class, matchDto.getTeam1Dto().getId());

			tempPoints = getPoints(winner, matchDto, dbCurrentTeamStats, dbTeam2Stats);
		}

		points = tempPoints;

		return points;
	}

	/**
	 * Liefert die gewonnene oder verlorene Punktzahl eines Teams bzw. Spielers.
	 * 
	 * @param winner <code>true</code> falls das Team bzw. Spieler gewonnen hat, andernfalls <code>false</code>.
	 * @param matchDto Das Spiel.
	 * @param db1Stats Die Teamspiel-, Doppelspiel- oder Einzelspiel-Statistik des ersten Teams bzw. Spielers.
	 * @param db2Stats Die Teamspiel-, Doppelspiel- oder Einzelspiel-Statistik des zweiten Teams bzw. Spielers.
	 * @return Die gewonnene oder verlorene Punktzahl eines Teams bzw Spielers.
	 */
	private static int getPoints(boolean winner, MatchDto matchDto, Stats db1Stats, Stats db2Stats) {
		final int points1 = db1Stats.getPoints();
		final int points2 = db2Stats.getPoints();

		final int pointsDifference = points1 - points2;
		final int pointsAbsDifference = Math.abs(points1 - points2);
		final boolean twoSetMatch = matchDto.getMatchSetsDto().getMatchSetsTeam1().size() == 2;

		int points = 0;
		if (winner) {
			if (pointsDifference >= 0) {
				if (twoSetMatch) {
					points = evaluatePositivePoints(points1, pointsAbsDifference, POINTS_A);
				} else {
					points = evaluatePositivePoints(points1, pointsAbsDifference, POINTS_B);
				}
			} else {
				if (!twoSetMatch) {
					points = evaluatePositivePoints(points1, pointsAbsDifference, POINTS_C);
				} else {
					points = evaluatePositivePoints(points1, pointsAbsDifference, POINTS_D);
				}
			}
		} else {
			if (pointsDifference <= 0) {
				if (twoSetMatch) {
					points = evaluateNegativePoints(points1, pointsAbsDifference, POINTS_A);
				} else {
					points = evaluateNegativePoints(points1, pointsAbsDifference, POINTS_B);
				}
			} else {
				if (!twoSetMatch) {
					points = evaluateNegativePoints(points1, pointsAbsDifference, POINTS_C);
				} else {
					points = evaluateNegativePoints(points1, pointsAbsDifference, POINTS_D);
				}
			}
		}
		return points;
	}

	/**
	 * Liefert die gewonnen Punkte eines Teams bzw. Spielers aufgrund der Punktedifferenz.
	 * 
	 * @param currentPoints Die aktuellen Punkte eines Teams bzw. Spielers.
	 * @param pointsAbsDifference Die absolute Punktedifferenz.
	 * @param pointsToMap Die Punkte welche das Teams bzw. Spielers aufgrund der Punktedifferenz gewinnen wird.
	 * @return Die gewonnen Punkte eines Teams bzw. Spielers aufgrund der Punktedifferenz.
	 */
	private static int evaluatePositivePoints(final int currentPoints, final int pointsAbsDifference, final int[][] pointsToMap) {
		int points = 0;
		if (pointsAbsDifference <= 25) {
			points = calculatePositivePoints(currentPoints, pointsToMap[0][0]);
		} else if (pointsAbsDifference > 25 && pointsAbsDifference <= 50) {
			points = calculatePositivePoints(currentPoints, pointsToMap[1][0]);
		} else if (pointsAbsDifference > 50 && pointsAbsDifference <= 100) {
			points = calculatePositivePoints(currentPoints, pointsToMap[2][0]);
		} else if (pointsAbsDifference > 100 && pointsAbsDifference <= 150) {
			points = calculatePositivePoints(currentPoints, pointsToMap[3][0]);
		} else if (pointsAbsDifference > 150 && pointsAbsDifference <= 225) {
			points = calculatePositivePoints(currentPoints, pointsToMap[4][0]);
		} else if (pointsAbsDifference > 225 && pointsAbsDifference <= 350) {
			points = calculatePositivePoints(currentPoints, pointsToMap[5][0]);
		} else if (pointsAbsDifference > 350 && pointsAbsDifference <= 500) {
			points = calculatePositivePoints(currentPoints, pointsToMap[6][0]);
		} else if (pointsAbsDifference > 500) {
			points = calculatePositivePoints(currentPoints, pointsToMap[7][0]);
		}
		return points;
	}

	/**
	 * Berechnet die finale Punktzahl anhand der Punkte welche dem Team bzw. Spieler hinzugefügt werden sollen und dem Multiplikator.
	 * 
	 * @param currentPoints Die aktuellen Punkte eines Teams bzw. Spielers.
	 * @param pointsToAdd Die Punkte welche dem Team bzw. Spieler hinzugefügt werden sollen.
	 * @return Berechnet die finale Punktzahl.
	 */
	private static int calculatePositivePoints(int currentPoints, int pointsToAdd) {
		final float multiplier = 1000 / (float) currentPoints;
		final float points = pointsToAdd * multiplier;

		return Math.round(points);
	}

	/**
	 * Liefert die verlorenen Punkte eines Teams bzw. Spielers aufgrund der Punktedifferenz.
	 * 
	 * @param currentPoints Die aktuellen Punkte eines Teams bzw. Spielers.
	 * @param pointsAbsDifference Die absolute Punktedifferenz.
	 * @param pointsToMap Die Punkte welche das Teams bzw. Spielers aufgrund der Punktedifferenz verlieren wird.
	 * @return Die verlorenen Punkte eines Teams bzw. Spielers aufgrund der Punktedifferenz.
	 */
	private static int evaluateNegativePoints(final int currentPoints, final int pointsAbsDifference, final int[][] pointsToMap) {
		int points = 0;
		if (pointsAbsDifference <= 25) {
			points = calculateNegativePoints(currentPoints, pointsToMap[0][1]);
		} else if (pointsAbsDifference > 25 && pointsAbsDifference <= 50) {
			points = calculateNegativePoints(currentPoints, pointsToMap[1][1]);
		} else if (pointsAbsDifference > 50 && pointsAbsDifference <= 100) {
			points = calculateNegativePoints(currentPoints, pointsToMap[2][1]);
		} else if (pointsAbsDifference > 100 && pointsAbsDifference <= 150) {
			points = calculateNegativePoints(currentPoints, pointsToMap[3][1]);
		} else if (pointsAbsDifference > 150 && pointsAbsDifference <= 225) {
			points = calculateNegativePoints(currentPoints, pointsToMap[4][1]);
		} else if (pointsAbsDifference > 225 && pointsAbsDifference <= 350) {
			points = calculateNegativePoints(currentPoints, pointsToMap[5][1]);
		} else if (pointsAbsDifference > 350 && pointsAbsDifference <= 500) {
			points = calculateNegativePoints(currentPoints, pointsToMap[6][1]);
		} else if (pointsAbsDifference > 500) {
			points = calculateNegativePoints(currentPoints, pointsToMap[7][1]);
		}
		return points;
	}

	/**
	 * Berechnet die finale Punktzahl anhand der Punkte welche dem Team bzw. Spieler abgezogen werden sollen und dem Multiplikator.
	 * 
	 * @param currentPoints Die aktuellen Punkte eines Teams bzw. Spielers.
	 * @param pointsToRemove Die Punkte welche dem Team bzw. Spieler abgezogen werden sollen.
	 * @return Berechnet die finale Punktzahl.
	 */
	private static int calculateNegativePoints(final int currentPoints, int pointsToRemove) {
		final float multiplier = (float) currentPoints / 1000;
		final float points = pointsToRemove * multiplier;

		return Math.round(points);
	}

}
