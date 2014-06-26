package de.kickerapp.server.services;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.ChartService;
import de.kickerapp.server.dao.Match;
import de.kickerapp.server.dao.SingleMatch;
import de.kickerapp.server.persistence.queries.MatchBean;
import de.kickerapp.server.services.MatchServiceHelper.MatchAscendingComparator;
import de.kickerapp.shared.dto.ChartDataDto;
import de.kickerapp.shared.dto.ChartDto;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Dienst zur Verarbeitung von Diagrammen im Klienten.
 * 
 * @author Sebastian Filke
 */
public class ChartServiceImpl extends RemoteServiceServlet implements ChartService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 4344447340775655248L;

	/** Die Monate. */
	private static final String[] MONTHS = new String[] { "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober",
			"November", "Dezember" };

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChartDto getSinglePlayerGoalChart(PlayerDto playerDto) throws IllegalArgumentException {
		final List<SingleMatch> dbSingleMatches = MatchBean.getSingleMatchesForPlayer(playerDto);
		Collections.sort(dbSingleMatches, new MatchAscendingComparator());

		final HashMap<Integer, ChartDataDto> chartData = new HashMap<Integer, ChartDataDto>();
		for (int i = 1; i <= 12; i++) {
			chartData.put(i, new ChartDataDto((long) i, MONTHS[i - 1]));
		}

		final Calendar calendar = Calendar.getInstance();
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		Integer winSeries = 0;
		Integer defeatSeries = 0;
		Integer tempWinSeries = 0;
		Integer tempDefeatSeries = 0;
		Integer maxWinPoints = 0;
		Integer maxDefeatPoints = 0;
		Integer maxPoints = 1000;
		Integer minPoints = 1000;
		Integer points = 1000;
		Integer sumWins = 0;
		Integer sumDefeats = 0;
		Integer sumPoints = 0;
		Integer sumMatches = 0;

		boolean containsPlayer = false;
		for (SingleMatch dbMatch : dbSingleMatches) {
			final int month = getMonthForMatch(calendar, dbMatch);
			final ChartDataDto chartDataDto = chartData.get(month);

			Integer shotGoals = chartDataDto.getShotGoals();
			Integer getGoals = chartDataDto.getGetGoals();
			Integer wins = chartDataDto.getWins();
			Integer defeats = chartDataDto.getDefeats();

			final boolean team1Winner = MatchServiceHelper.isTeam1Winner(dbMatch);
			if (dbMatch.getPlayer1().getKey().getId() == playerDto.getId()) {
				containsPlayer = true;
				final Integer matchPoints = dbMatch.getMatchPoints().getMatchPointsTeam1().get(0);
				if (maxWinPoints < matchPoints) {
					maxWinPoints = matchPoints;
				} else if (maxDefeatPoints > matchPoints) {
					maxDefeatPoints = matchPoints;
				}
				points = points + matchPoints;
				if (points > maxPoints) {
					maxPoints = points;
				} else if (points < minPoints) {
					minPoints = points;
				}
				sumPoints = sumPoints + points;
				for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam1()) {
					shotGoals = shotGoals + matchSet;
				}
				for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam2()) {
					getGoals = getGoals + matchSet;
				}
				if (team1Winner) {
					wins++;
					sumWins++;
					tempDefeatSeries = 0;
					tempWinSeries++;
					if (winSeries < tempWinSeries) {
						winSeries = tempWinSeries;
					}
				} else {
					defeats++;
					sumDefeats++;
					tempWinSeries = 0;
					tempDefeatSeries++;
					if (defeatSeries < tempDefeatSeries) {
						defeatSeries = tempDefeatSeries;
					}
				}
				sumMatches++;
			} else if (dbMatch.getPlayer2().getKey().getId() == playerDto.getId()) {
				containsPlayer = true;
				final Integer matchPoints = dbMatch.getMatchPoints().getMatchPointsTeam2().get(0);
				if (maxWinPoints < matchPoints) {
					maxWinPoints = matchPoints;
				} else if (maxDefeatPoints > matchPoints) {
					maxDefeatPoints = matchPoints;
				}
				points = points + matchPoints;
				if (points > maxPoints) {
					maxPoints = points;
				} else if (points < minPoints) {
					minPoints = points;
				}
				sumPoints = sumPoints + points;
				for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam2()) {
					shotGoals = shotGoals + matchSet;
				}
				for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam1()) {
					getGoals = getGoals + matchSet;
				}
				if (!team1Winner) {
					wins++;
					sumWins++;
					tempDefeatSeries = 0;
					tempWinSeries++;
					if (winSeries < tempWinSeries) {
						winSeries = tempWinSeries;
					}
				} else {
					defeats++;
					sumDefeats++;
					tempWinSeries = 0;
					tempDefeatSeries++;
					if (defeatSeries < tempDefeatSeries) {
						defeatSeries = tempDefeatSeries;
					}
				}
				sumMatches++;
			}
			chartDataDto.setShotGoals(shotGoals);
			chartDataDto.setGetGoals(getGoals);
			chartDataDto.setWins(wins);
			chartDataDto.setDefeats(defeats);
		}
		final ChartDto chartDto = new ChartDto();

		final ArrayList<ChartDataDto> chartDtos = new ArrayList<ChartDataDto>();
		if (containsPlayer) {
			chartDto.setWinSeries(winSeries);
			chartDto.setDefeatSeries(defeatSeries);
			chartDto.setMaxWinPoints(maxWinPoints);
			chartDto.setMaxDefeatPoints(maxDefeatPoints);
			chartDto.setMaxPoints(maxPoints);
			chartDto.setMinPoints(minPoints);
			final double averageWins = ((double) (sumWins * 100)) / sumMatches;
			chartDto.setAverageWins(numberFormat.format(averageWins));
			final double averagePoints = ((double) sumPoints / sumMatches);
			chartDto.setAveragePoints(numberFormat.format(averagePoints));

			for (ChartDataDto chartDataDto : chartData.values()) {
				chartDtos.add(chartDataDto);
			}
		}
		chartDto.setChartDataDto(chartDtos);

		return chartDto;
	}

	/**
	 * Liefert eine Zahl zwischen 1 und 12 zur Representation des Monats.
	 * 
	 * @param calendar Der Kalender.
	 * @param dbMatch Das Spiel.
	 * @return Eine Zahl zwischen 1 und 12 zur Representation des Monats.
	 */
	private int getMonthForMatch(Calendar calendar, Match dbMatch) {
		calendar.setTime(dbMatch.getMatchDate());

		return calendar.get(Calendar.MONTH) + 1;
	}

}
