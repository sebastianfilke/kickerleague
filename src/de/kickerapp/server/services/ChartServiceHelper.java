package de.kickerapp.server.services;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.kickerapp.server.dao.DoubleMatchHistory;
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
	private static int getMonthForMatch(MatchHistory dbHistory) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(dbHistory.getMatchDate());

		return calendar.get(Calendar.MONTH) + 1;
	}

	protected static Integer getMatchWinSeries(List<? extends MatchHistory> dbMatchHistories) {
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

	protected static Integer getMatchDefeatSeries(List<? extends MatchHistory> dbMatchHistories) {
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

	protected static Integer getMatchMaxWinPoints(List<? extends MatchHistory> dbMatchHistories) {
		Integer maxWinPoints = 0;

		for (MatchHistory dbHistory : dbMatchHistories) {
			if (maxWinPoints < dbHistory.getMatchPoints()) {
				maxWinPoints = dbHistory.getMatchPoints();
			}
		}
		return maxWinPoints;
	}

	protected static Integer getMatchMaxLostPoints(List<? extends MatchHistory> dbMatchHistories) {
		Integer maxLostPoints = 0;

		for (MatchHistory dbHistory : dbMatchHistories) {
			if (maxLostPoints > dbHistory.getMatchPoints()) {
				maxLostPoints = dbHistory.getMatchPoints();
			}
		}
		return maxLostPoints;
	}

	protected static Integer getMatchMaxPoints(List<? extends MatchHistory> dbMatchHistories) {
		Integer maxPoints = 1000;

		for (MatchHistory dbHistory : dbMatchHistories) {
			if (maxPoints < dbHistory.getTotalPoints()) {
				maxPoints = dbHistory.getTotalPoints();
			}
		}
		return maxPoints;
	}

	protected static Integer getMatchMinPoints(List<? extends MatchHistory> dbMatchHistories) {
		Integer minPoints = 1000;

		for (MatchHistory dbHistory : dbMatchHistories) {
			if (minPoints > dbHistory.getTotalPoints()) {
				minPoints = dbHistory.getTotalPoints();
			}
		}
		return minPoints;
	}

	protected static String getPercentageWins(int wins, int defeats) {
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		final Integer sumMatches = wins + defeats;
		final double percentageWins = ((double) (wins * 100)) / sumMatches;

		return numberFormat.format(percentageWins);
	}

	protected static String getAveragePoints(List<? extends MatchHistory> dbMatchHistories, int wins, int defeats) {
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		Integer sumPoints = 0;

		for (MatchHistory dbHistory : dbMatchHistories) {
			sumPoints = sumPoints + dbHistory.getTotalPoints();
		}
		final Integer sumMatches = wins + defeats;
		final double averagePoints = (double) sumPoints / sumMatches;

		return numberFormat.format(averagePoints);
	}

	protected static String getAverageTablePlace(List<? extends MatchHistory> dbMatchHistories, int wins, int defeats) {
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		Integer sumTablePlace = 0;

		for (MatchHistory dbHistory : dbMatchHistories) {
			sumTablePlace = sumTablePlace + dbHistory.getTablePlace();
		}

		final Integer sumMatches = wins + defeats;
		final double averageTablePlace = (double) sumTablePlace / sumMatches;

		return numberFormat.format(averageTablePlace);
	}

	protected static void updateGameForMatch(MatchHistory dbHistory, HashMap<Integer, ChartGameDto> chartGameDtos) {
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

	protected static void updateGoalForMatch(MatchHistory dbHistory, HashMap<Integer, ChartGoalDto> chartGoalDtos) {
		final int month = ChartServiceHelper.getMonthForMatch(dbHistory);

		final ChartGoalDto chartDataDto = chartGoalDtos.get(month);

		Integer shotGoals = chartDataDto.getShotGoals();
		Integer getGoals = chartDataDto.getGetGoals();

		shotGoals = shotGoals + dbHistory.getShotGoals();
		getGoals = getGoals + dbHistory.getGetGoals();

		chartDataDto.setShotGoals(shotGoals);
		chartDataDto.setGetGoals(getGoals);
	}

	protected static void updateOpponentForMatch(SingleMatchHistory dbHistory, HashMap<Long, ChartOpponentDto> chartOpponentDtos) {
		final Player opponentPlayer2 = dbHistory.getPlayer2();

		updateOpponentForMatch(opponentPlayer2, dbHistory, chartOpponentDtos);
	}

	protected static void updateOpponentForMatch(DoubleMatchHistory dbHistory, HashMap<Long, ChartOpponentDto> chartOpponentDtos) {
		final Player opponentPlayer3 = dbHistory.getPlayer3();
		final Player opponentPlayer4 = dbHistory.getPlayer4();

		updateOpponentForMatch(opponentPlayer3, dbHistory, chartOpponentDtos);
		updateOpponentForMatch(opponentPlayer4, dbHistory, chartOpponentDtos);
	}

	private static void updateOpponentForMatch(Player opponent, MatchHistory dbHistory, HashMap<Long, ChartOpponentDto> chartOpponentDtos) {
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

	protected static void updatePointForMatch(MatchHistory dbHistory, ArrayList<ChartPointDto> chartPointDtos) {
		final ChartPointDto chartPointDto = new ChartPointDto();
		chartPointDto.setId(chartPointDtos.size() + 1);
		chartPointDto.setMatchNumber(dbHistory.getMatchNumber());
		chartPointDto.setPoints(dbHistory.getTotalPoints());

		chartPointDtos.add(chartPointDto);
	}

	protected static int getWinsForHistory(List<? extends MatchHistory> dbHistories) {
		int sumWins = 0;

		for (MatchHistory dbHistory : dbHistories) {
			if (dbHistory.isWinner()) {
				sumWins++;
			}
		}
		return sumWins;
	}

	protected static int getDefeatsForHistory(List<? extends MatchHistory> dbHistories) {
		int sumDefeats = 0;

		for (MatchHistory dbHistory : dbHistories) {
			if (!dbHistory.isWinner()) {
				sumDefeats++;
			}
		}
		return sumDefeats;
	}

}
