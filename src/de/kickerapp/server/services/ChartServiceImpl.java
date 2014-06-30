package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.ChartService;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.SingleMatchHistory;
import de.kickerapp.server.dao.fetchplans.PlayerPlan;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.persistence.queries.MatchBean;
import de.kickerapp.shared.dto.ChartGoalDataDto;
import de.kickerapp.shared.dto.InfoDto;
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
	public InfoDto getSinglePlayerInfo(PlayerDto playerDto) throws IllegalArgumentException {
		final List<SingleMatchHistory> dbSingleMatches = MatchBean.getSingleMatchesForPlayer(playerDto);

		final Player dbPlayer = PMFactory.getObjectById(Player.class, playerDto.getId(), PlayerPlan.PLAYERSINGLESTATS);

		final InfoDto infoDto = new InfoDto();
		infoDto.setWinSeries(ChartServiceHelper.getSingleMatchWinSeries(dbSingleMatches));
		infoDto.setDefeatSeries(ChartServiceHelper.getSingleMatchDefeatSeries(dbSingleMatches));
		infoDto.setMaxWinPoints(ChartServiceHelper.getSingleMatchMaxWinPoints(dbSingleMatches));
		infoDto.setMaxLostPoints(ChartServiceHelper.getSingleMatchMaxLostPoints(dbSingleMatches));
		infoDto.setMaxPoints(ChartServiceHelper.getSingleMatchMaxPoints(dbSingleMatches));
		infoDto.setMinPoints(ChartServiceHelper.getSingleMatchMinPoints(dbSingleMatches));
		infoDto.setAverageWins(ChartServiceHelper.getSingleAverageWins(dbSingleMatches, dbPlayer));
		infoDto.setAveragePoints(ChartServiceHelper.getSingleAveragePoints(dbSingleMatches, dbPlayer));

		return infoDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<ChartGoalDataDto> getSinglePlayerGoalChart(PlayerDto playerDto) throws IllegalArgumentException {
		final List<SingleMatchHistory> dbSingleMatches = MatchBean.getSingleMatchesForPlayer(playerDto);

		return ChartServiceHelper.getChartDataDto(dbSingleMatches);
	}

}
