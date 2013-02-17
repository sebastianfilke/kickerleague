package de.kickerapp.server.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import de.kickerapp.shared.dto.MatchSetDto;
import de.kickerapp.shared.dto.TeamDto;

public class MatchServiceHelper {

	/**
	 * Comparator zur Sortierung der Betriebsmittelarten.
	 * 
	 * @author Sebastian Filke, GIGATRONIK München GmbH
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

	public static MatchDto createMatch(Match dbMatch) {
		final MatchDto matchDto = new MatchDto();
		matchDto.setId(dbMatch.getKey().getId());
		matchDto.setMatchNumber(Long.toString(dbMatch.getMatchNumber()));
		matchDto.setMatchDate(dbMatch.getMatchDate());
		matchDto.setMatchType(dbMatch.getMatchType());

		final MatchType matchType = MatchType.None;
		if (dbMatch.getMatchType() == MatchType.Single) {
			final Player team1Player1 = PMFactory.getObjectById(Player.class, dbMatch.getTeam1());
			matchDto.setTeam1(new TeamDto(PlayerServiceHelper.createPlayer(team1Player1, matchType)));

			final Player team2Player2 = PMFactory.getObjectById(Player.class, dbMatch.getTeam2());
			matchDto.setTeam2(new TeamDto(PlayerServiceHelper.createPlayer(team2Player2, matchType)));
		} else {
			final Team team1 = PMFactory.getObjectById(Team.class, dbMatch.getTeam1());

			final Player team1Player1 = PMFactory.getObjectById(Player.class, (Long) team1.getPlayers().toArray()[0]);
			final Player team1Player2 = PMFactory.getObjectById(Player.class, (Long) team1.getPlayers().toArray()[1]);

			matchDto.setTeam1(new TeamDto(PlayerServiceHelper.createPlayer(team1Player1, matchType), PlayerServiceHelper.createPlayer(team1Player2, matchType)));

			final Team team2 = PMFactory.getObjectById(Team.class, dbMatch.getTeam2());

			final Player team2Player1 = PMFactory.getObjectById(Player.class, (Long) team2.getPlayers().toArray()[0]);
			final Player team2Player2 = PMFactory.getObjectById(Player.class, (Long) team2.getPlayers().toArray()[1]);

			matchDto.setTeam2(new TeamDto(PlayerServiceHelper.createPlayer(team2Player1, matchType), PlayerServiceHelper.createPlayer(team2Player2, matchType)));
		}
		final MatchSetDto setDto = new MatchSetDto();
		setDto.setSetsTeam1(dbMatch.getMatchSets().getSetsTeam1());
		setDto.setSetsTeam2(dbMatch.getMatchSets().getSetsTeam2());
		matchDto.setSets(setDto);

		return matchDto;
	}

	protected static void updateSingleStats() {
		final List<PlayerSingleStats> dbPlayersSingleStats = PMFactory.getList(PlayerSingleStats.class);
		removeStatsWithZeroMatches(dbPlayersSingleStats);

		Collections.sort(dbPlayersSingleStats, new StatsComparator());
		final int size = dbPlayersSingleStats.size();
		for (int i = 0; i < size; i++) {
			PlayerSingleStats singleStats = dbPlayersSingleStats.get(i);
			updateStats(i, singleStats);
			singleStats = PMFactory.persistObject(singleStats);
		}
	}

	protected static void updateDoubleStats() {
		final List<PlayerDoubleStats> dbPlayersDoubleStats = PMFactory.getList(PlayerDoubleStats.class);
		removeStatsWithZeroMatches(dbPlayersDoubleStats);

		Collections.sort(dbPlayersDoubleStats, new StatsComparator());
		final int size = dbPlayersDoubleStats.size();
		for (int i = 0; i < size; i++) {
			PlayerDoubleStats doubleStats = dbPlayersDoubleStats.get(i);
			updateStats(i, doubleStats);
			doubleStats = PMFactory.persistObject(doubleStats);
		}
	}

	protected static void updateTeamStats() {
		final List<TeamStats> dbTeamStats = PMFactory.getList(TeamStats.class);
		removeStatsWithZeroMatches(dbTeamStats);

		Collections.sort(dbTeamStats, new StatsComparator());
		final int size = dbTeamStats.size();
		for (int i = 0; i < size; i++) {
			TeamStats teamStats = dbTeamStats.get(i);
			updateStats(i, teamStats);
			teamStats = PMFactory.persistObject(teamStats);
		}
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

	private static int getPoints(boolean winner, Player dbPlayer, MatchDto matchDto) {
		final boolean twoSetGame = matchDto.getSets().getSetsTeam1().size() == 2;

		int points = 0;
		if (winner) {
			if (twoSetGame) {
				points = points + 8;
			} else {
				points = points + 6;
			}
		} else {
			if (twoSetGame) {
				points = points - 8;
			} else {
				points = points - 6;
			}
		}
		return points;
	}

}
