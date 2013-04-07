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
import de.kickerapp.server.entity.Match;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.services.MatchServiceHelper.MatchAscendingComparator;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.ChartDataDto;
import de.kickerapp.shared.dto.ChartDto;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Dienst zur Verarbeitung von Charts im Klienten.
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
	public ChartDto getGoalChart(PlayerDto playerDto) throws IllegalArgumentException {
		final List<Match> dbMatches = PMFactory.getList(Match.class);
		Collections.sort(dbMatches, new MatchAscendingComparator());

		final HashMap<Integer, ChartDataDto> chartData = new HashMap<Integer, ChartDataDto>();
		for (int i = 1; i <= 12; i++) {
			chartData.put(i, new ChartDataDto((long) i, MONTHS[i - 1]));
		}

		final Calendar calendar = Calendar.getInstance();
		final NumberFormat numberFormat = new DecimalFormat("0.0");
		numberFormat.setRoundingMode(RoundingMode.DOWN);

		Integer winSeries = 0;
		Integer lossSeries = 0;
		Integer tempWinSeries = 0;
		Integer tempLossSeries = 0;
		Integer maxWinPoints = 0;
		Integer maxLossPoints = 0;
		Integer maxPoints = 1000;
		Integer minPoints = 1000;
		Integer points = 1000;
		Integer sumWins = 0;
		Integer sumLosses = 0;
		Integer sumPoints = 0;
		Integer sumMatches = 0;

		boolean containsPlayer = false;
		for (Match dbMatch : dbMatches) {
			final int month = getMonthForMatch(calendar, dbMatch);
			final ChartDataDto chartDataDto = chartData.get(month);

			Integer shotGoals = chartDataDto.getShotGoals();
			Integer getGoals = chartDataDto.getGetGoals();
			Integer wins = chartDataDto.getWins();
			Integer losses = chartDataDto.getLosses();

			final boolean team1Winner = MatchServiceHelper.isTeam1Winner(dbMatch);
			if (dbMatch.getMatchType() == MatchType.SINGLE) {
				if (dbMatch.getTeam1().equals(playerDto.getId())) {
					containsPlayer = true;
					final Integer matchPoints = dbMatch.getMatchPoints().getMatchPointsTeam1().get(0);
					if (maxWinPoints < matchPoints) {
						maxWinPoints = matchPoints;
					} else if (maxLossPoints > matchPoints) {
						maxLossPoints = matchPoints;
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
						tempLossSeries = 0;
						tempWinSeries++;
						if (winSeries < tempWinSeries) {
							winSeries = tempWinSeries;
						}
					} else {
						losses++;
						sumLosses++;
						tempWinSeries = 0;
						tempLossSeries++;
						if (lossSeries < tempLossSeries) {
							lossSeries = tempLossSeries;
						}
					}
					sumMatches++;
				} else if (dbMatch.getTeam2().equals(playerDto.getId())) {
					containsPlayer = true;
					final Integer matchPoints = dbMatch.getMatchPoints().getMatchPointsTeam2().get(0);
					if (maxWinPoints < matchPoints) {
						maxWinPoints = matchPoints;
					} else if (maxLossPoints > matchPoints) {
						maxLossPoints = matchPoints;
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
						tempLossSeries = 0;
						tempWinSeries++;
						if (winSeries < tempWinSeries) {
							winSeries = tempWinSeries;
						}
					} else {
						losses++;
						sumLosses++;
						tempWinSeries = 0;
						tempLossSeries++;
						if (lossSeries < tempLossSeries) {
							lossSeries = tempLossSeries;
						}
					}
					sumMatches++;
				}
			}
			chartDataDto.setShotGoals(shotGoals);
			chartDataDto.setGetGoals(getGoals);
			chartDataDto.setWins(wins);
			chartDataDto.setLosses(losses);
		}
		final ChartDto chartDto = new ChartDto();

		final ArrayList<ChartDataDto> chartDtos = new ArrayList<ChartDataDto>();
		if (containsPlayer) {
			chartDto.setWinSeries(winSeries);
			chartDto.setLossSeries(lossSeries);
			chartDto.setMaxWinPoints(maxWinPoints);
			chartDto.setMaxLossPoints(maxLossPoints);
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
