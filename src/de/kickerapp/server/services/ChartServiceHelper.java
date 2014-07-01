package de.kickerapp.server.services;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.kickerapp.server.dao.MatchHistory;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.SingleMatchHistory;
import de.kickerapp.shared.dto.ChartGameDto;
import de.kickerapp.shared.dto.ChartGoalDto;
import de.kickerapp.shared.dto.ChartOpponentDto;

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
			chartOpponentDto.setPlayedGames(1);

			if (dbHistory.isWinner()) {
				final int wins = chartOpponentDto.getWins() + 1;
				chartOpponentDto.setWins(wins);
			} else {
				final int defeats = chartOpponentDto.getDefeats() + 1;
				chartOpponentDto.setDefeats(defeats);
			}
			chartOpponentDtos.put(opponent.getKey().getId(), chartOpponentDto);
		} else {
			final int playedGames = chartOpponentDto.getPlayedGames() + 1;
			chartOpponentDto.setPlayedGames(playedGames);

			if (dbHistory.isWinner()) {
				final int wins = chartOpponentDto.getWins() + 1;
				chartOpponentDto.setWins(wins);
			} else {
				final int defeats = chartOpponentDto.getDefeats() + 1;
				chartOpponentDto.setDefeats(defeats);
			}
		}
	}

}
