package de.kickerapp.server.services;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.kickerapp.server.dao.Match;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.SingleMatch;
import de.kickerapp.shared.dto.ChartDataDto;
import de.kickerapp.shared.dto.PlayerDto;

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
	 * @param dbMatch Das Spiel.
	 * @return Eine Zahl zwischen 1 und 12 zur Representation des Monats.
	 */
	protected static int getMonthForMatch(Match dbMatch) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(dbMatch.getMatchDate());

		return calendar.get(Calendar.MONTH) + 1;
	}

	protected static Integer getSingleMatchWinSeries(List<SingleMatch> dbSingleMatches, Player dbPlayer) {
		Integer winSeries = 0;
		Integer tempWinSeries = 0;

		for (SingleMatch dbMatch : dbSingleMatches) {
			final boolean team1Winner = MatchServiceHelper.isTeam1Winner(dbMatch);
			if (dbMatch.getPlayer1().getKey().getId() == dbPlayer.getKey().getId()) {
				if (team1Winner) {
					tempWinSeries++;
				} else {
					tempWinSeries = 0;
				}
			} else if (dbMatch.getPlayer2().getKey().getId() == dbPlayer.getKey().getId()) {
				if (!team1Winner) {
					tempWinSeries++;
				} else {
					tempWinSeries = 0;
				}
			}
			if (winSeries < tempWinSeries) {
				winSeries = tempWinSeries;
			}
		}
		return winSeries;
	}

	protected static Integer getSingleMatchDefeatSeries(List<SingleMatch> dbSingleMatches, Player dbPlayer) {
		Integer defeatSeries = 0;
		Integer tempDefeatSeries = 0;

		for (SingleMatch dbMatch : dbSingleMatches) {
			final boolean team1Winner = MatchServiceHelper.isTeam1Winner(dbMatch);
			if (dbMatch.getPlayer1().getKey().getId() == dbPlayer.getKey().getId()) {
				if (team1Winner) {
					tempDefeatSeries = 0;
				} else {
					tempDefeatSeries++;
				}
			} else if (dbMatch.getPlayer2().getKey().getId() == dbPlayer.getKey().getId()) {
				if (!team1Winner) {
					tempDefeatSeries = 0;
				} else {
					tempDefeatSeries++;
				}
			}
			if (defeatSeries < tempDefeatSeries) {
				defeatSeries = tempDefeatSeries;
			}
		}
		return defeatSeries;
	}

	public static Integer getSingleMatchMaxWinPoints(List<SingleMatch> dbSingleMatches, Player dbPlayer) {
		Integer maxWinPoints = 0;

		for (SingleMatch dbMatch : dbSingleMatches) {
			if (dbMatch.getPlayer1().getKey().getId() == dbPlayer.getKey().getId()) {
				final Integer matchPoints = dbMatch.getMatchPoints().getMatchPointsTeam1().get(0);
				if (maxWinPoints < matchPoints) {
					maxWinPoints = matchPoints;
				}
			} else if (dbMatch.getPlayer2().getKey().getId() == dbPlayer.getKey().getId()) {
				final Integer matchPoints = dbMatch.getMatchPoints().getMatchPointsTeam2().get(0);
				if (maxWinPoints < matchPoints) {
					maxWinPoints = matchPoints;
				}
			}
		}
		return maxWinPoints;
	}

	public static Integer getSingleMatchMaxLostPoints(List<SingleMatch> dbSingleMatches, Player dbPlayer) {
		Integer maxLostPoints = 0;

		for (SingleMatch dbMatch : dbSingleMatches) {
			if (dbMatch.getPlayer1().getKey().getId() == dbPlayer.getKey().getId()) {
				final Integer matchPoints = dbMatch.getMatchPoints().getMatchPointsTeam1().get(0);
				if (maxLostPoints > matchPoints) {
					maxLostPoints = matchPoints;
				}
			} else if (dbMatch.getPlayer2().getKey().getId() == dbPlayer.getKey().getId()) {
				final Integer matchPoints = dbMatch.getMatchPoints().getMatchPointsTeam2().get(0);
				if (maxLostPoints > matchPoints) {
					maxLostPoints = matchPoints;
				}
			}
		}
		return maxLostPoints;
	}

	public static Integer getSingleMatchMaxPoints(List<SingleMatch> dbSingleMatches, Player dbPlayer) {
		Integer maxPoints = 1000;
		Integer points = 1000;

		for (SingleMatch dbMatch : dbSingleMatches) {
			if (dbMatch.getPlayer1().getKey().getId() == dbPlayer.getKey().getId()) {
				final Integer matchPoints = dbMatch.getMatchPoints().getMatchPointsTeam1().get(0);
				points = points + matchPoints;
			} else if (dbMatch.getPlayer2().getKey().getId() == dbPlayer.getKey().getId()) {
				final Integer matchPoints = dbMatch.getMatchPoints().getMatchPointsTeam2().get(0);
				points = points + matchPoints;
			}
			if (points > maxPoints) {
				maxPoints = points;
			}
		}
		return maxPoints;
	}

	public static Integer getSingleMatchMinPoints(List<SingleMatch> dbSingleMatches, Player dbPlayer) {
		Integer minPoints = 1000;
		Integer points = 1000;

		for (SingleMatch dbMatch : dbSingleMatches) {
			if (dbMatch.getPlayer1().getKey().getId() == dbPlayer.getKey().getId()) {
				final Integer matchPoints = dbMatch.getMatchPoints().getMatchPointsTeam1().get(0);
				points = points + matchPoints;
			} else if (dbMatch.getPlayer2().getKey().getId() == dbPlayer.getKey().getId()) {
				final Integer matchPoints = dbMatch.getMatchPoints().getMatchPointsTeam2().get(0);
				points = points + matchPoints;
			}
			if (points < minPoints) {
				minPoints = points;
			}
		}
		return minPoints;
	}

	public static String getSingleAverageWins(List<SingleMatch> dbSingleMatches, Player dbPlayer) {
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		Integer sumWins = 0;

		for (SingleMatch dbMatch : dbSingleMatches) {
			final boolean team1Winner = MatchServiceHelper.isTeam1Winner(dbMatch);
			if (dbMatch.getPlayer1().getKey().getId() == dbPlayer.getKey().getId()) {
				if (team1Winner) {
					sumWins++;
				}
			} else if (dbMatch.getPlayer2().getKey().getId() == dbPlayer.getKey().getId()) {
				if (!team1Winner) {
					sumWins++;
				}
			}
		}
		final Integer sumMatches = dbPlayer.getPlayerSingleStats().getWins() + dbPlayer.getPlayerSingleStats().getDefeats();
		final double averageWins = ((double) (sumWins * 100)) / sumMatches;

		return numberFormat.format(averageWins);
	}

	public static String getSingleAveragePoints(List<SingleMatch> dbSingleMatches, Player dbPlayer) {
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		Integer sumPoints = 0;
		Integer points = 1000;

		for (SingleMatch dbMatch : dbSingleMatches) {
			if (dbMatch.getPlayer1().getKey().getId() == dbPlayer.getKey().getId()) {
				final Integer matchPoints = dbMatch.getMatchPoints().getMatchPointsTeam1().get(0);
				points = points + matchPoints;
				sumPoints = sumPoints + points;
			} else if (dbMatch.getPlayer2().getKey().getId() == dbPlayer.getKey().getId()) {
				final Integer matchPoints = dbMatch.getMatchPoints().getMatchPointsTeam2().get(0);
				points = points + matchPoints;
				sumPoints = sumPoints + points;
			}
		}
		final Integer sumMatches = dbPlayer.getPlayerSingleStats().getWins() + dbPlayer.getPlayerSingleStats().getDefeats();
		final double averagePoints = ((double) sumPoints / sumMatches);

		return numberFormat.format(averagePoints);
	}

	protected static ArrayList<ChartDataDto> getChartDataDto(PlayerDto playerDto, final List<SingleMatch> dbSingleMatches) {
		final HashMap<Integer, ChartDataDto> chartData = new HashMap<Integer, ChartDataDto>();
		for (int i = 1; i <= 12; i++) {
			chartData.put(i, new ChartDataDto((long) i, MONTHS[i - 1]));
		}

		final ArrayList<ChartDataDto> chartDataDtos = new ArrayList<ChartDataDto>();
		for (SingleMatch dbMatch : dbSingleMatches) {
			final int month = ChartServiceHelper.getMonthForMatch(dbMatch);
			final ChartDataDto chartDataDto = chartData.get(month);

			Integer shotGoals = chartDataDto.getShotGoals();
			Integer getGoals = chartDataDto.getGetGoals();
			Integer wins = chartDataDto.getWins();
			Integer defeats = chartDataDto.getDefeats();

			final boolean team1Winner = MatchServiceHelper.isTeam1Winner(dbMatch);
			if (dbMatch.getPlayer1().getKey().getId() == playerDto.getId()) {
				for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam1()) {
					shotGoals = shotGoals + matchSet;
				}
				for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam2()) {
					getGoals = getGoals + matchSet;
				}
				if (team1Winner) {
					wins++;
				} else {
					defeats++;
				}
			} else if (dbMatch.getPlayer2().getKey().getId() == playerDto.getId()) {
				for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam2()) {
					shotGoals = shotGoals + matchSet;
				}
				for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam1()) {
					getGoals = getGoals + matchSet;
				}
				if (!team1Winner) {
					wins++;
				} else {
					defeats++;
				}
			}
			chartDataDto.setShotGoals(shotGoals);
			chartDataDto.setGetGoals(getGoals);
			chartDataDto.setWins(wins);
			chartDataDto.setDefeats(defeats);
		}
		for (ChartDataDto chartDataDto : chartData.values()) {
			chartDataDtos.add(chartDataDto);
		}
		return chartDataDtos;
	}

}
