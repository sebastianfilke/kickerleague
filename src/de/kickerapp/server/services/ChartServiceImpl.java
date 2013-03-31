package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.ChartService;
import de.kickerapp.server.entity.Match;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.common.MatchType;
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
	public ArrayList<ChartDto> getGoalChart(PlayerDto playerDto) throws IllegalArgumentException {
		final List<Match> dbMatches = PMFactory.getList(Match.class);

		final HashMap<Integer, ChartDto> data = new HashMap<Integer, ChartDto>();
		for (int i = 1; i <= 12; i++) {
			data.put(i, new ChartDto(MONTHS[i - 1]));
		}

		final Calendar calendar = Calendar.getInstance();

		boolean containsPlayer = false;
		for (Match dbMatch : dbMatches) {
			final int month = getMonthForMatch(calendar, dbMatch);
			final ChartDto chartDto = data.get(month);

			Integer shotGoals = chartDto.getShotGoals();
			Integer getGoals = chartDto.getGetGoals();

			if (dbMatch.getMatchType() == MatchType.SINGLE) {
				if (dbMatch.getTeam1().equals(playerDto.getId())) {
					containsPlayer = true;
					for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam1()) {
						shotGoals = shotGoals + matchSet;
					}
					for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam2()) {
						getGoals = getGoals + matchSet;
					}
				} else if (dbMatch.getTeam2().equals(playerDto.getId())) {
					containsPlayer = true;
					for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam2()) {
						shotGoals = shotGoals + matchSet;
					}
					for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam1()) {
						getGoals = getGoals + matchSet;
					}
				}
			}
			chartDto.setShotGoals(shotGoals);
			chartDto.setGetGoals(getGoals);
		}
		final ArrayList<ChartDto> chartDtos = new ArrayList<ChartDto>();
		if (containsPlayer) {
			for (ChartDto chartDto : data.values()) {
				chartDtos.add(chartDto);
			}
		}
		return chartDtos;
	}

	/**
	 * Liefert eine Zahl zwischen 1 und 12 zur Representation des Monats.
	 * 
	 * @param calendar Der Kalender.
	 * @param dbMatch Das Spiel.
	 * @return Eine Zahl zwischen 1 und 12 zur Representation des Monats.
	 */
	private int getMonthForMatch(Calendar calendar, Match dbMatch) {
		dbMatch.getMatchDate().getTime();
		calendar.setTime(dbMatch.getMatchDate());

		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<ChartDto> getWinChart(PlayerDto playerDto) throws IllegalArgumentException {
		final List<Match> dbMatches = PMFactory.getList(Match.class);

		final HashMap<Integer, ChartDto> data = new HashMap<Integer, ChartDto>();
		for (int i = 1; i <= 12; i++) {
			data.put(i, new ChartDto(MONTHS[i - 1]));
		}

		final Calendar calendar = Calendar.getInstance();

		boolean containsPlayer = false;
		for (Match dbMatch : dbMatches) {
			final int month = getMonthForMatch(calendar, dbMatch);
			final ChartDto chartDto = data.get(month);

			Integer wins = chartDto.getWins();
			Integer losses = chartDto.getLosses();

			final boolean team1Winner = MatchServiceHelper.isTeam1Winner(dbMatch);
			if (dbMatch.getMatchType() == MatchType.SINGLE) {
				if (dbMatch.getTeam1().equals(playerDto.getId())) {
					containsPlayer = true;
					if (team1Winner) {
						wins = wins + 1;
					} else {
						losses = losses + 1;
					}
				} else if (dbMatch.getTeam2().equals(playerDto.getId())) {
					containsPlayer = true;
					if (!team1Winner) {
						wins = wins + 1;
					} else {
						losses = losses + 1;
					}
				}
			}
			chartDto.setWins(wins);
			chartDto.setLosses(losses);
		}
		final ArrayList<ChartDto> chartDtos = new ArrayList<ChartDto>();
		if (containsPlayer) {
			for (ChartDto chartDto : data.values()) {
				chartDtos.add(chartDto);
			}
		}
		return chartDtos;
	}

}
