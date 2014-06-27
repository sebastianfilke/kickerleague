package de.kickerapp.server.services;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.ChartService;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.SingleMatch;
import de.kickerapp.server.dao.fetchplans.PlayerPlan;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.persistence.queries.MatchBean;
import de.kickerapp.server.services.MatchServiceHelper.MatchAscendingComparator;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChartDto getSinglePlayerGoalChart(PlayerDto playerDto) throws IllegalArgumentException {
		final List<SingleMatch> dbSingleMatches = MatchBean.getSingleMatchesForPlayer(playerDto);
		Collections.sort(dbSingleMatches, new MatchAscendingComparator());

		final Player dbPlayer = PMFactory.getObjectById(Player.class, playerDto.getId(), PlayerPlan.PLAYERSINGLESTATS);

		final ChartDto chartDto = new ChartDto();
		chartDto.setWinSeries(ChartServiceHelper.getSingleMatchWinSeries(dbSingleMatches, dbPlayer));
		chartDto.setDefeatSeries(ChartServiceHelper.getSingleMatchDefeatSeries(dbSingleMatches, dbPlayer));
		chartDto.setMaxWinPoints(ChartServiceHelper.getSingleMatchMaxWinPoints(dbSingleMatches, dbPlayer));
		chartDto.setMaxLostPoints(ChartServiceHelper.getSingleMatchMaxLostPoints(dbSingleMatches, dbPlayer));
		chartDto.setMaxPoints(ChartServiceHelper.getSingleMatchMaxPoints(dbSingleMatches, dbPlayer));
		chartDto.setMinPoints(ChartServiceHelper.getSingleMatchMinPoints(dbSingleMatches, dbPlayer));
		chartDto.setAverageWins(ChartServiceHelper.getSingleAverageWins(dbSingleMatches, dbPlayer));
		chartDto.setAveragePoints(ChartServiceHelper.getSingleAveragePoints(dbSingleMatches, dbPlayer));
		chartDto.setChartDataDto(ChartServiceHelper.getChartDataDto(playerDto, dbSingleMatches));

		return chartDto;
	}

}
