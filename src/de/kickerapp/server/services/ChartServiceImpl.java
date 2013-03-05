package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.ChartService;
import de.kickerapp.server.entity.Match;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.ChartDto;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Dienst zur Verarbeitung von Charts im Clienten.
 * 
 * @author Sebastian Filke
 */
public class ChartServiceImpl extends RemoteServiceServlet implements ChartService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 4344447340775655248L;

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

		final Calendar cal = Calendar.getInstance();

		for (Match dbMatch : dbMatches) {
			final int month = getMonthForMatch(cal, dbMatch);
			final ChartDto chartDto = data.get(month);

			Integer shotGoals = chartDto.getShotGoals();
			Integer getGoals = chartDto.getGetGoals();

			if (dbMatch.getMatchType() == MatchType.SINGLE) {
				if (dbMatch.getTeam1().equals(playerDto.getId())) {
					for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam1()) {
						shotGoals = shotGoals + matchSet;
					}
					for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam2()) {
						getGoals = getGoals + matchSet;
					}
				} else if (dbMatch.getTeam2().equals(playerDto.getId())) {
					for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam2()) {
						shotGoals = shotGoals + matchSet;
					}
					for (Integer matchSet : dbMatch.getMatchSets().getMatchSetsTeam1()) {
						getGoals = getGoals + matchSet;
					}
				}
			}
		}
		final ArrayList<ChartDto> chartDto = (ArrayList<ChartDto>) data.values();

		return chartDto;
	}

	private int getMonthForMatch(Calendar cal, Match dbMatch) {
		dbMatch.getMatchDate().getTime();
		cal.setTime(dbMatch.getMatchDate());

		return cal.get(Calendar.MONTH) + 1;
	}
}
