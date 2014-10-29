package de.kickerapp.server.services;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.kickerapp.server.dao.MatchHistory;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.SingleMatchHistory;
import de.kickerapp.shared.dto.ChartGameDto;
import de.kickerapp.shared.dto.ChartGoalDto;
import de.kickerapp.shared.dto.ChartOpponentDto;
import de.kickerapp.shared.dto.ChartPointDto;

/**
 * Hilfsklasse für den Dienst zur Verarbeitung von Diagrammen im Klienten.
 * 
 * @author Sebastian Filke
 */
public class ChartServiceHelper {

	/**
	 * Liefert eine Zahl zwischen 1 und 12 zur Representation des Monats.
	 * 
	 * @param dbHistory Der Verlauf des Spiel.
	 * @return Eine Zahl zwischen 1 und 12 zur Representation des Monats.
	 */
	protected static int getMonthForMatch(MatchHistory dbHistory) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(dbHistory.getMatchDate());

		return calendar.get(Calendar.MONTH) + 1;
	}

	public static Integer getMatchWinSeries(List<? extends MatchHistory> dbMatchHistories) {
		Integer winSeries = 0;
		Integer tempWinSeries = 0;

		for (MatchHistory dbHistory : dbMatchHistories) {
			if (dbHistory.isWinner()) {
				tempWinSeries++;
			} else {
				tempWinSeries = 0;
			}
			if (winSeries < tempWinSeries) {
				winSeries = tempWinSeries;
			}
		}
		return winSeries;
	}

	public static Integer getMatchDefeatSeries(List<? extends MatchHistory> dbMatchHistories) {
		Integer defeatSeries = 0;
		Integer tempDefeatSeries = 0;

		for (MatchHistory dbHistory : dbMatchHistories) {
			if (dbHistory.isWinner()) {
				tempDefeatSeries = 0;
			} else {
				tempDefeatSeries++;
			}
			if (defeatSeries < tempDefeatSeries) {
				defeatSeries = tempDefeatSeries;
			}
		}
		return defeatSeries;
	}

	public static Integer getMatchMaxWinPoints(List<? extends MatchHistory> dbMatchHistories) {
		Integer maxWinPoints = 0;

		for (MatchHistory dbHistory : dbMatchHistories) {
			if (maxWinPoints < dbHistory.getMatchPoints()) {
				maxWinPoints = dbHistory.getMatchPoints();
			}
		}
		return maxWinPoints;
	}

	public static Integer getMatchMaxLostPoints(List<? extends MatchHistory> dbMatchHistories) {
		Integer maxLostPoints = 0;

		for (MatchHistory dbHistory : dbMatchHistories) {
			if (maxLostPoints > dbHistory.getMatchPoints()) {
				maxLostPoints = dbHistory.getMatchPoints();
			}
		}
		return maxLostPoints;
	}

	public static Integer getMatchMaxPoints(List<? extends MatchHistory> dbMatchHistories) {
		Integer maxPoints = 1000;

		for (MatchHistory dbHistory : dbMatchHistories) {
			if (maxPoints < dbHistory.getTotalPoints()) {
				maxPoints = dbHistory.getTotalPoints();
			}
		}
		return maxPoints;
	}

	public static Integer getMatchMinPoints(List<? extends MatchHistory> dbMatchHistories) {
		Integer minPoints = 1000;

		for (MatchHistory dbHistory : dbMatchHistories) {
			if (minPoints > dbHistory.getTotalPoints()) {
				minPoints = dbHistory.getTotalPoints();
			}
		}
		return minPoints;
	}

	public static String getPercentageWins(Player dbPlayer, int wins, int defeats) {
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		final Integer sumMatches = wins + defeats;
		final double percentageWins = ((double) (wins * 100)) / sumMatches;

		return numberFormat.format(percentageWins);
	}

	public static String getAveragePoints(List<? extends MatchHistory> dbMatchHistories, Player dbPlayer, int wins, int defeats) {
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		Integer sumPoints = 0;

		for (MatchHistory dbHistory : dbMatchHistories) {
			sumPoints = sumPoints + dbHistory.getTotalPoints();
		}
		final Integer sumMatches = wins + defeats;
		final double averagePoints = ((double) sumPoints / sumMatches);

		return numberFormat.format(averagePoints);
	}

	public static String getAverageTablePlace(List<? extends MatchHistory> dbMatchHistories, Player dbPlayer, int wins, int defeats) {
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		Integer sumTablePlace = 0;

		for (MatchHistory dbHistory : dbMatchHistories) {
			sumTablePlace = sumTablePlace + dbHistory.getTablePlace();
		}

		final Integer sumMatches = wins + defeats;
		final double averageTablePlace = ((double) sumTablePlace / sumMatches);

		return numberFormat.format(averageTablePlace);
	}

	public static void updateGameForMatch(MatchHistory dbHistory, HashMap<Integer, ChartGameDto> chartGameDtos) {
		final int month = ChartServiceHelper.getMonthForMatch(dbHistory);

		final ChartGameDto chartDataDto = chartGameDtos.get(month);

		Integer wins = chartDataDto.getWins();
		Integer defeats = chartDataDto.getDefeats();

		if (dbHistory.isWinner()) {
			wins++;
		} else {
			defeats++;
		}
		chartDataDto.setWins(wins);
		chartDataDto.setDefeats(defeats);
	}

	public static void updateGoalForMatch(MatchHistory dbHistory, HashMap<Integer, ChartGoalDto> chartGoalDtos) {
		final int month = ChartServiceHelper.getMonthForMatch(dbHistory);

		final ChartGoalDto chartDataDto = chartGoalDtos.get(month);

		Integer shotGoals = chartDataDto.getShotGoals();
		Integer getGoals = chartDataDto.getGetGoals();

		shotGoals = shotGoals + dbHistory.getShotGoals();
		getGoals = getGoals + dbHistory.getGetGoals();

		chartDataDto.setShotGoals(shotGoals);
		chartDataDto.setGetGoals(getGoals);
	}

	public static void updateOpponentForMatch(SingleMatchHistory dbHistory, HashMap<Long, ChartOpponentDto> chartOpponentDtos) {
		final Player opponent = dbHistory.getPlayer2();

		ChartOpponentDto chartOpponentDto = chartOpponentDtos.get(opponent.getKey().getId());
		if (chartOpponentDto == null) {
			chartOpponentDto = new ChartOpponentDto();
			chartOpponentDto.setId(opponent.getKey().getId());
			chartOpponentDto.setOpponentName(opponent.getLastName() + ", " + opponent.getFirstName());

			chartOpponentDtos.put(opponent.getKey().getId(), chartOpponentDto);
		}

		if (dbHistory.isWinner()) {
			final int wins = chartOpponentDto.getWins() + 1;
			chartOpponentDto.setWins(wins);
		} else {
			final int defeats = chartOpponentDto.getDefeats() + 1;
			chartOpponentDto.setDefeats(defeats);
		}
		updatePercentages(chartOpponentDtos);
	}

	private static void updatePercentages(HashMap<Long, ChartOpponentDto> chartOpponentDtos) {
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		final int sumMatches = getCurrentSumMatches(chartOpponentDtos);

		for (ChartOpponentDto chartOpponentDto : chartOpponentDtos.values()) {
			final int wins = chartOpponentDto.getWins();
			final int defeats = chartOpponentDto.getDefeats();

			final int sumMatchesForOpponent = wins + defeats;
			final double percentageWins = ((double) (wins * 100)) / sumMatchesForOpponent;
			final double percentageDefeats = ((double) (defeats * 100)) / sumMatchesForOpponent;
			final double percentageMatches = ((double) (sumMatchesForOpponent * 100)) / sumMatches;

			chartOpponentDto.setPercentageWins(numberFormat.format(percentageWins));
			chartOpponentDto.setPercentageDefeats(numberFormat.format(percentageDefeats));
			chartOpponentDto.setPercentageMatches(numberFormat.format(percentageMatches));
		}
	}

	private static int getCurrentSumMatches(HashMap<Long, ChartOpponentDto> chartOpponentDtos) {
		int sumMatches = 0;
		for (ChartOpponentDto chartOpponentDto : chartOpponentDtos.values()) {
			sumMatches = sumMatches + chartOpponentDto.getWins() + chartOpponentDto.getDefeats();
		}
		return sumMatches;
	}

	public static void updatePointForMatch(MatchHistory dbHistory, ArrayList<ChartPointDto> chartPointDtos) {
		final ChartPointDto chartPointDto = new ChartPointDto();
		chartPointDto.setId(chartPointDtos.size() + 1);
		chartPointDto.setMatchNumber(dbHistory.getMatchNumber());
		chartPointDto.setPoints(dbHistory.getTotalPoints());

		chartPointDtos.add(chartPointDto);
	}

	public static int getWinsForHistory(List<? extends MatchHistory> dbHistories) {
		int sumWins = 0;

		for (MatchHistory dbHistory : dbHistories) {
			if (dbHistory.isWinner()) {
				sumWins++;
			}
		}
		return sumWins;
	}

	public static int getDefeatsForHistory(List<? extends MatchHistory> dbHistories) {
		int sumDefeats = 0;

		for (MatchHistory dbHistory : dbHistories) {
			if (!dbHistory.isWinner()) {
				sumDefeats++;
			}
		}
		return sumDefeats;
	}

}
