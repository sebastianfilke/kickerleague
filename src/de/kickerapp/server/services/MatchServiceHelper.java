package de.kickerapp.server.services;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.core.client.util.DateWrapper.Unit;

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
	 * @return Die Client-Datenklasse.
	 */
	public static MatchDto createDtoMatch(Match dbMatch) {
		final MatchDto matchDto = new MatchDto();
		matchDto.setId(dbMatch.getKey().getId());
		matchDto.setMatchNumber(dbMatch.getMatchNumber());
		matchDto.setMatchDate(dbMatch.getMatchDate());
		matchDto.setMatchType(dbMatch.getMatchType());

		final SimpleDateFormat dateFormat = new SimpleDateFormat("'Spiele vom' EEEE, 'den' dd.MM.yyyy", Locale.GERMAN);
		matchDto.setGroupDate(dateFormat.format(dbMatch.getMatchDate()));

		final MatchType matchType = MatchType.NONE;
		if (dbMatch.getMatchType() == MatchType.SINGLE) {
			final Player team1Player1 = PMFactory.getObjectById(Player.class, dbMatch.getTeam1());
			matchDto.setTeam1Dto(new TeamDto(PlayerServiceHelper.createDtoPlayer(team1Player1, matchType)));

			final Player team2Player2 = PMFactory.getObjectById(Player.class, dbMatch.getTeam2());
			matchDto.setTeam2(new TeamDto(PlayerServiceHelper.createDtoPlayer(team2Player2, matchType)));
		} else {
			final Team team1 = PMFactory.getObjectById(Team.class, dbMatch.getTeam1());

			final Player team1Player1 = PMFactory.getObjectById(Player.class, (Long) team1.getPlayers().toArray()[0]);
			final Player team1Player2 = PMFactory.getObjectById(Player.class, (Long) team1.getPlayers().toArray()[1]);

			matchDto.setTeam1Dto(new TeamDto(PlayerServiceHelper.createDtoPlayer(team1Player1, matchType), PlayerServiceHelper.createDtoPlayer(team1Player2,
					matchType)));

			final Team team2 = PMFactory.getObjectById(Team.class, dbMatch.getTeam2());

			final Player team2Player1 = PMFactory.getObjectById(Player.class, (Long) team2.getPlayers().toArray()[0]);
			final Player team2Player2 = PMFactory.getObjectById(Player.class, (Long) team2.getPlayers().toArray()[1]);

			matchDto.setTeam2(new TeamDto(PlayerServiceHelper.createDtoPlayer(team2Player1, matchType), PlayerServiceHelper.createDtoPlayer(team2Player2,
					matchType)));
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

	protected static int getGoalsTeam1(MatchDto match) {
		int goals = 0;
		for (Integer result : match.getMatchSetsDto().getMatchSetsTeam1()) {
			if (result != null) {
				goals = goals + result;
			}
		}
		return goals;
	}

	protected static int getGoalsTeam2(MatchDto match) {
		int goals = 0;
		for (Integer result : match.getMatchSetsDto().getMatchSetsTeam2()) {
			if (result != null) {
				goals = goals + result;
			}
		}
		return goals;
	}

	protected static void updateTable(MatchDto matchDto) {
		if (matchDto.getMatchType() == MatchType.SINGLE) {
			updateSingleStats();
		} else {
			updateDoubleStats();
			updateTeamStats();
		}
	}

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

	private static void removeStatsWithZeroMatches(List<? extends Stats> dbStats) {
		final ArrayList<Stats> statsToRemove = new ArrayList<Stats>();
		for (Stats stat : dbStats) {
			if (stat.getWins() == 0 && stat.getLosses() == 0) {
				statsToRemove.add(stat);
			}
		}
		dbStats.removeAll(statsToRemove);
	}

	private static void updateStats(int i, Stats stats) {
		stats.setPrevTablePlace(stats.getCurTablePlace());
		stats.setCurTablePlace(i + 1);

		final int preTablePlace = stats.getPrevTablePlace();
		final int curTablePlace = stats.getCurTablePlace();

		if (preTablePlace == 0) {
			stats.setTendency(Tendency.Upward);
		} else {
			if (curTablePlace == preTablePlace) {
				stats.setTendency(Tendency.Constant);
			} else if (curTablePlace > preTablePlace) {
				stats.setTendency(Tendency.Downward);
			} else if (curTablePlace < preTablePlace) {
				stats.setTendency(Tendency.Upward);
			}
		}
	}

	protected static int getPointsForPlayer(boolean winner, Player dbPlayer, Stats dbPlayerStats, MatchDto matchDto) {
		int points = 0;

		points = getPointsForLastMatchDate(winner, dbPlayer.getLastMatchDate(), points);
		points = getPointsForTablePlace(winner, dbPlayer, dbPlayerStats, matchDto, points);
		points = getPointsForNumberOfSets(winner, matchDto, points);

		return points;
	}

	protected static int getPointsForTeam(boolean winner, Team dbTeam, Stats dbTeamStats, MatchDto matchDto) {
		int points = 0;

		points = getPointsForLastMatchDate(winner, dbTeam.getLastMatchDate(), points);
		points = getPointsForTablePlace(winner, dbTeam, dbTeamStats, matchDto, points);
		points = getPointsForNumberOfSets(winner, matchDto, points);

		return points;
	}

	private static int getPointsForLastMatchDate(boolean winner, Date lastMatchDate, int points) {
		final Date lastMatchCurDay = new DateWrapper().clearTime().asDate();
		final Date lastMatch1day = new DateWrapper().clearTime().add(Unit.DAY, -1).asDate();
		final Date lastMatch3days = new DateWrapper().clearTime().add(Unit.DAY, -3).asDate();
		final Date lastMatch6days = new DateWrapper().clearTime().add(Unit.DAY, -6).asDate();
		final Date lastMatch9days = new DateWrapper().clearTime().add(Unit.DAY, -9).asDate();
		final Date lastMatch14days = new DateWrapper().clearTime().add(Unit.DAY, -14).asDate();
		final Date lastMatch28days = new DateWrapper().clearTime().add(Unit.DAY, -28).asDate();

		if (lastMatchDate != null) {
			final Date lastMatchDay = new DateWrapper(lastMatchDate).clearTime().asDate();
			if (lastMatchDay.compareTo(lastMatchCurDay) == 0) {
				points = points + 1;
			} else if (lastMatchDay.after(lastMatch1day) && lastMatchDay.before(lastMatch3days)) {
				points = points + 2;
			} else if (lastMatchDay.after(lastMatch3days) && lastMatchDay.before(lastMatch6days)) {
				points = points + 3;
			} else if (lastMatchDay.after(lastMatch6days) && lastMatchDay.before(lastMatch9days)) {
				points = points + 4;
			} else if (lastMatchDay.after(lastMatch9days) && lastMatchDay.before(lastMatch14days)) {
				points = points + 6;
			} else if (lastMatchDay.after(lastMatch14days) && lastMatchDay.before(lastMatch28days)) {
				points = points + 8;
			} else {
				points = points + 10;
			}
		} else {
			if (winner) {
				points = points + 6;
			} else {
				points = points - 6;
			}
		}
		return points;
	}

	private static int getPointsForTablePlace(boolean winner, Player dbPlayer, Stats dbPlayerStats, MatchDto matchDto, int points) {
		if (matchDto.getMatchType() == MatchType.SINGLE) {
			final PlayerSingleStats dbPlayer1SingleStats = (PlayerSingleStats) dbPlayerStats;
			PlayerSingleStats dbPlayer2SingleStats = null;
			if (matchDto.getTeam1Dto().getPlayer1().getId() == dbPlayer.getKey().getId()) {
				dbPlayer2SingleStats = PMFactory.getObjectById(PlayerSingleStats.class, matchDto.getTeam2Dto().getPlayer1().getId());
			} else {
				dbPlayer2SingleStats = PMFactory.getObjectById(PlayerSingleStats.class, matchDto.getTeam1Dto().getPlayer1().getId());
			}

			points = getPointsForTablePlaceDifference(winner, points, dbPlayer1SingleStats, dbPlayer2SingleStats);
		} else {
			final PlayerDoubleStats dbTeam1Player1SingleStats = (PlayerDoubleStats) dbPlayerStats;
			PlayerDoubleStats dbTeam2Player1SingleStats = null;
			PlayerDoubleStats dbTeam2Player2SingleStats = null;
			if (matchDto.getTeam1Dto().getPlayer1().getId() == dbPlayer.getKey().getId()) {
				dbTeam2Player1SingleStats = PMFactory.getObjectById(PlayerDoubleStats.class, matchDto.getTeam2Dto().getPlayer1().getId());
				dbTeam2Player2SingleStats = PMFactory.getObjectById(PlayerDoubleStats.class, matchDto.getTeam2Dto().getPlayer2().getId());
			} else {
				dbTeam2Player1SingleStats = PMFactory.getObjectById(PlayerDoubleStats.class, matchDto.getTeam1Dto().getPlayer1().getId());
				dbTeam2Player2SingleStats = PMFactory.getObjectById(PlayerDoubleStats.class, matchDto.getTeam1Dto().getPlayer2().getId());
			}

			points = getPointsForTablePlaceDifference(winner, points, dbTeam1Player1SingleStats, dbTeam2Player1SingleStats, dbTeam2Player2SingleStats);
		}
		return points;
	}

	private static int getPointsForTablePlace(boolean winner, Team dbTeam, Stats dbTeamStats, MatchDto matchDto, int points) {
		final TeamStats dbTeam1Stats = (TeamStats) dbTeamStats;
		TeamStats dbTeam2Stats = null;

		if (matchDto.getTeam1Dto().getId() == dbTeam.getKey().getId()) {
			dbTeam2Stats = PMFactory.getObjectById(TeamStats.class, matchDto.getTeam2Dto().getId());
		} else {
			dbTeam2Stats = PMFactory.getObjectById(TeamStats.class, matchDto.getTeam1Dto().getId());
		}
		points = getPointsForTablePlaceDifference(winner, points, dbTeam1Stats, dbTeam2Stats);

		return points;
	}

	private static int getPointsForTablePlaceDifference(boolean winner, int points, final Stats stats1, final Stats stats2) {
		final int differenceTablePlace = stats1.getCurTablePlace() - stats2.getCurTablePlace();

		points = getPointsForTablePlace(winner, points, differenceTablePlace);
		return points;
	}

	private static int getPointsForTablePlaceDifference(boolean winner, int points, Stats dbTeam1Player1Stats, Stats dbTeam2Player1Stats,
			Stats dbTeam2Player2Stats) {
		final double middleTablePlace = (dbTeam2Player1Stats.getCurTablePlace() + dbTeam2Player2Stats.getCurTablePlace()) / 2;
		final int ceiledMiddleTablePlace = (int) Math.ceil(middleTablePlace);

		final int differenceTablePlace = dbTeam1Player1Stats.getCurTablePlace() - ceiledMiddleTablePlace;

		points = getPointsForTablePlace(winner, points, differenceTablePlace);

		return points;
	}

	private static int getPointsForTablePlace(boolean winner, int points, final int differenceTablePlace) {
		if (winner) {
			if (differenceTablePlace <= 0) {
				points = points + 2;
				if (differenceTablePlace <= -1 && differenceTablePlace >= -3) {
					points = points + 4;
				} else if (differenceTablePlace <= -4 && differenceTablePlace >= -8) {
					points = points + 2;
				} else if (differenceTablePlace < -8) {
					points = points + 1;
				}
			} else {
				points = points + 4;
				if (differenceTablePlace >= 1 && differenceTablePlace <= 3) {
					points = points + 1;
				} else if (differenceTablePlace >= 4 && differenceTablePlace <= 8) {
					points = points + 2;
				} else if (differenceTablePlace > 8) {
					points = points + 4;
				}
			}
		} else {
			if (differenceTablePlace <= 0) {
				points = points - 2;
				if (differenceTablePlace <= -1 && differenceTablePlace >= -3) {
					points = points - 1;
				} else if (differenceTablePlace <= -4 && differenceTablePlace >= -8) {
					points = points - 2;
				} else if (differenceTablePlace < -8) {
					points = points - 4;
				}
			} else {
				points = points - 4;
				if (differenceTablePlace >= 1 && differenceTablePlace <= 3) {
					points = points - 4;
				} else if (differenceTablePlace >= 4 && differenceTablePlace <= 8) {
					points = points - 2;
				} else if (differenceTablePlace > 8) {
					points = points - 1;
				}
			}
		}
		return points;
	}

	private static int getPointsForNumberOfSets(boolean winner, MatchDto matchDto, int points) {
		final boolean twoSetGame = matchDto.getMatchSetsDto().getMatchSetsTeam1().size() == 2;
		if (winner) {
			if (twoSetGame) {
				points = points + 6;
			} else {
				points = points + 4;
			}
		} else {
			if (twoSetGame) {
				points = points - 6;
			} else {
				points = points - 4;
			}
		}
		return points;
	}

}
