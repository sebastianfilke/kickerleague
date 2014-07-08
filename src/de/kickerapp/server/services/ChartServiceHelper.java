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

	public static Integer getSingleMatchWinSeries(List<SingleMatchHistory> dbSingleMatches) {
		Integer winSeries = 0;
		Integer tempWinSeries = 0;

		for (SingleMatchHistory dbHistory : dbSingleMatches) {
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

	public static Integer getSingleMatchDefeatSeries(List<SingleMatchHistory> dbSingleMatches) {
		Integer defeatSeries = 0;
		Integer tempDefeatSeries = 0;

		for (SingleMatchHistory dbHistory : dbSingleMatches) {
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

	public static Integer getSingleMatchMaxWinPoints(List<SingleMatchHistory> dbSingleMatches) {
		Integer maxWinPoints = 0;

		for (SingleMatchHistory dbHistory : dbSingleMatches) {
			if (maxWinPoints < dbHistory.getMatchPoints()) {
				maxWinPoints = dbHistory.getMatchPoints();
			}
		}
		return maxWinPoints;
	}

	public static Integer getSingleMatchMaxLostPoints(List<SingleMatchHistory> dbSingleMatches) {
		Integer maxLostPoints = 0;

		for (SingleMatchHistory dbHistory : dbSingleMatches) {
			if (maxLostPoints > dbHistory.getMatchPoints()) {
				maxLostPoints = dbHistory.getMatchPoints();
			}
		}
		return maxLostPoints;
	}

	public static Integer getSingleMatchMaxPoints(List<SingleMatchHistory> dbSingleMatches) {
		Integer maxPoints = 1000;

		for (SingleMatchHistory dbHistory : dbSingleMatches) {
			if (maxPoints < dbHistory.getTotalPoints()) {
				maxPoints = dbHistory.getTotalPoints();
			}
		}
		return maxPoints;
	}

	public static Integer getSingleMatchMinPoints(List<SingleMatchHistory> dbSingleMatches) {
		Integer minPoints = 1000;

		for (SingleMatchHistory dbHistory : dbSingleMatches) {
			if (minPoints > dbHistory.getTotalPoints()) {
				minPoints = dbHistory.getTotalPoints();
			}
		}
		return minPoints;
	}

	public static String getSinglePercentageWins(List<SingleMatchHistory> dbSingleMatches, Player dbPlayer) {
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		final Integer sumMatches = dbPlayer.getPlayerSingleStats().getWins() + dbPlayer.getPlayerSingleStats().getDefeats();
		final double percentageWins = ((double) (dbPlayer.getPlayerSingleStats().getWins() * 100)) / sumMatches;

		return numberFormat.format(percentageWins);
	}

	public static String getSingleAveragePoints(List<SingleMatchHistory> dbSingleMatches, Player dbPlayer) {
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		Integer sumPoints = 0;

		for (SingleMatchHistory dbHistory : dbSingleMatches) {
			sumPoints = sumPoints + dbHistory.getTotalPoints();
		}
		final Integer sumMatches = dbPlayer.getPlayerSingleStats().getWins() + dbPlayer.getPlayerSingleStats().getDefeats();
		final double averagePoints = ((double) sumPoints / sumMatches);

		return numberFormat.format(averagePoints);
	}

	public static String getSingleAverageTablePlace(List<SingleMatchHistory> dbSingleMatches, Player dbPlayer) {
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		Integer sumTablePlace = 0;

		for (SingleMatchHistory dbHistory : dbSingleMatches) {
			sumTablePlace = sumTablePlace + dbHistory.getTablePlace();
		}

		final Integer sumMatches = dbPlayer.getPlayerSingleStats().getWins() + dbPlayer.getPlayerSingleStats().getDefeats();
		final double averageTablePlace = ((double) sumTablePlace / sumMatches);

		return numberFormat.format(averageTablePlace);
	}

	public static void updateGameForMatch(SingleMatchHistory dbHistory, HashMap<Integer, ChartGameDto> chartGameDtos) {
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

	public static void updateGoalForMatch(SingleMatchHistory dbHistory, HashMap<Integer, ChartGoalDto> chartGoalDtos) {
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

	public static void updatePointForMatch(SingleMatchHistory dbHistory, ArrayList<ChartPointDto> chartPointDtos) {
		final ChartPointDto chartPointDto = new ChartPointDto();
		chartPointDto.setId(chartPointDtos.size() + 1);
		chartPointDto.setMatchNumber(dbHistory.getMatchNumber());
		chartPointDto.setPoints(dbHistory.getTotalPoints());

		chartPointDtos.add(chartPointDto);
	}

}