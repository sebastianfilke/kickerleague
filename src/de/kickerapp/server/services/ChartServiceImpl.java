package de.kickerapp.server.services;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.ChartService;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.SingleMatchHistory;
import de.kickerapp.server.dao.fetchplans.PlayerPlan;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.persistence.queries.MatchBean;
import de.kickerapp.shared.container.ChartContainer;
import de.kickerapp.shared.dto.ChartGameDto;
import de.kickerapp.shared.dto.ChartGoalDto;
import de.kickerapp.shared.dto.ChartOpponentDto;
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

	/** Die Monate. */
	private static final String[] MONTHS = new String[] { "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober",
			"November", "Dezember" };

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
	public ChartContainer getSinglePlayerChart(PlayerDto playerDto) throws IllegalArgumentException {
		final List<SingleMatchHistory> dbSingleMatches = MatchBean.getSingleMatchesForPlayer(playerDto);

		final HashMap<Integer, ChartGameDto> chartGameDtos = new HashMap<Integer, ChartGameDto>();
		final HashMap<Integer, ChartGoalDto> chartGoalDtos = new HashMap<Integer, ChartGoalDto>();
		for (int i = 1; i <= 12; i++) {
			chartGameDtos.put(i, new ChartGameDto((long) i, MONTHS[i - 1]));
			chartGoalDtos.put(i, new ChartGoalDto((long) i, MONTHS[i - 1]));
		}
		final HashMap<Long, ChartOpponentDto> chartOpponentDtos = new HashMap<Long, ChartOpponentDto>();

		for (SingleMatchHistory dbHistory : dbSingleMatches) {
			ChartServiceHelper.updateGameForMatch(dbHistory, chartGameDtos);
			ChartServiceHelper.updateGoalForMatch(dbHistory, chartGoalDtos);
			ChartServiceHelper.updateOpponentForMatch(dbHistory, chartOpponentDtos);
		}

		final ChartContainer container = new ChartContainer();
		container.getChartGameDtos().addAll(chartGameDtos.values());
		container.getChartGoalDtos().addAll(chartGoalDtos.values());
		container.getChartOpponentDtos().addAll(chartOpponentDtos.values());

		return container;
	}

}
