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
import de.kickerapp.shared.dto.ChartGoalDataDto;

/**
 * Hilfsklasse für den Dienst zur Verarbeitung von Diagrammen im Klienten.
 * 
 * @author Sebastian Filke
 */
public class ChartServiceHelper {

	/** Die Monate. */
	private static final String[] MONTHS = new String[] { "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober",
			"November", "Dezember" };

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
		Integer maxPoints = 0;

		for (SingleMatchHistory dbHistory : dbSingleMatches) {
			if (maxPoints < dbHistory.getTotalPoints()) {
				maxPoints = dbHistory.getTotalPoints();
			}
		}
		return maxPoints;
	}

	public static Integer getSingleMatchMinPoints(List<SingleMatchHistory> dbSingleMatches) {
		Integer minPoints = 0;

		for (SingleMatchHistory dbHistory : dbSingleMatches) {
			if (minPoints > dbHistory.getTotalPoints()) {
				minPoints = dbHistory.getTotalPoints();
			}
		}
		return minPoints;
	}

	public static String getSingleAverageWins(List<SingleMatchHistory> dbSingleMatches, Player dbPlayer) {
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		final Integer sumMatches = dbPlayer.getPlayerSingleStats().getWins() + dbPlayer.getPlayerSingleStats().getDefeats();
		final double averageWins = ((double) (dbPlayer.getPlayerSingleStats().getWins() * 100)) / sumMatches;

		return numberFormat.format(averageWins);
	}

	public static String getSingleAveragePoints(List<SingleMatchHistory> dbSingleMatches, Player dbPlayer) {
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		Integer sumPoints = 0;
		Integer points = 1000;

		for (SingleMatchHistory dbHistory : dbSingleMatches) {
			points = points + dbHistory.getMatchPoints();
			sumPoints = sumPoints + points;
		}
		final Integer sumMatches = dbPlayer.getPlayerSingleStats().getWins() + dbPlayer.getPlayerSingleStats().getDefeats();
		final double averagePoints = ((double) sumPoints / sumMatches);

		return numberFormat.format(averagePoints);
	}

	public static ArrayList<ChartGoalDataDto> getChartDataDto(List<SingleMatchHistory> dbSingleMatches) {
		final HashMap<Integer, ChartGoalDataDto> chartData = new HashMap<Integer, ChartGoalDataDto>();
		for (int i = 1; i <= 12; i++) {
			chartData.put(i, new ChartGoalDataDto((long) i, MONTHS[i - 1]));
		}

		final ArrayList<ChartGoalDataDto> chartDataDtos = new ArrayList<ChartGoalDataDto>();
		for (SingleMatchHistory dbHistory : dbSingleMatches) {
			final int month = ChartServiceHelper.getMonthForMatch(dbHistory);
			final ChartGoalDataDto chartDataDto = chartData.get(month);

			Integer shotGoals = chartDataDto.getShotGoals();
			Integer getGoals = chartDataDto.getGetGoals();
			// Integer wins = chartDataDto.getWins();
			// Integer defeats = chartDataDto.getDefeats();

			shotGoals = shotGoals + dbHistory.getShotGoals();
			getGoals = getGoals + dbHistory.getGetGoals();
			// if (dbHistory.isWinner()) {
			// wins++;
			// } else {
			// defeats++;
			// }
			chartDataDto.setShotGoals(shotGoals);
			chartDataDto.setGetGoals(getGoals);
			// chartDataDto.setWins(wins);
			// chartDataDto.setDefeats(defeats);
		}
		for (ChartGoalDataDto chartDataDto : chartData.values()) {
			chartDataDtos.add(chartDataDto);
		}
		return chartDataDtos;
	}

}
