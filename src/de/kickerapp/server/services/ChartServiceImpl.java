package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.ChartService;
import de.kickerapp.server.dao.DoubleMatchHistory;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.SingleMatchHistory;
import de.kickerapp.server.dao.fetchplans.PlayerPlan;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.persistence.queries.MatchBean;
import de.kickerapp.shared.container.ChartContainer;
import de.kickerapp.shared.dto.ChartGameDto;
import de.kickerapp.shared.dto.ChartGoalDto;
import de.kickerapp.shared.dto.ChartOpponentDto;
import de.kickerapp.shared.dto.ChartPointDto;
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
	public InfoDto getSinglePlayerInfo(PlayerDto playerDto, Integer year) throws IllegalArgumentException {
		final List<SingleMatchHistory> dbSingleMatches = MatchBean.getSingleMatchesForPlayer(playerDto, year);

		final Player dbPlayer = PMFactory.getObjectById(Player.class, playerDto.getId(), PlayerPlan.PLAYERSINGLESTATS);

		final InfoDto infoDto = new InfoDto();
		infoDto.setWinSeries(ChartServiceHelper.getMatchWinSeries(dbSingleMatches));
		infoDto.setDefeatSeries(ChartServiceHelper.getMatchDefeatSeries(dbSingleMatches));
		infoDto.setMaxWinPoints(ChartServiceHelper.getMatchMaxWinPoints(dbSingleMatches));
		infoDto.setMaxLostPoints(ChartServiceHelper.getMatchMaxLostPoints(dbSingleMatches));
		infoDto.setMaxPoints(ChartServiceHelper.getMatchMaxPoints(dbSingleMatches));
		infoDto.setMinPoints(ChartServiceHelper.getMatchMinPoints(dbSingleMatches));
		infoDto.setPercentageWins(ChartServiceHelper.getPercentageWins(dbPlayer));
		infoDto.setAveragePoints(ChartServiceHelper.getAveragePoints(dbSingleMatches, dbPlayer));
		infoDto.setAverageTablePlace(ChartServiceHelper.getAverageTablePlace(dbSingleMatches, dbPlayer));

		return infoDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChartContainer getSinglePlayerChart(PlayerDto playerDto, Integer year) throws IllegalArgumentException {
		final List<SingleMatchHistory> dbSingleMatches = MatchBean.getSingleMatchesForPlayer(playerDto, year);

		final HashMap<Integer, ChartGameDto> chartGameDtos = new HashMap<Integer, ChartGameDto>();
		final HashMap<Integer, ChartGoalDto> chartGoalDtos = new HashMap<Integer, ChartGoalDto>();
		for (int i = 1; i <= 12; i++) {
			chartGameDtos.put(i, new ChartGameDto((long) i, MONTHS[i - 1]));
			chartGoalDtos.put(i, new ChartGoalDto((long) i, MONTHS[i - 1]));
		}
		final HashMap<Long, ChartOpponentDto> chartOpponentDtos = new HashMap<Long, ChartOpponentDto>();
		final ArrayList<ChartPointDto> chartPointDtos = new ArrayList<ChartPointDto>();

		for (SingleMatchHistory dbHistory : dbSingleMatches) {
			ChartServiceHelper.updateGameForMatch(dbHistory, chartGameDtos);
			ChartServiceHelper.updateGoalForMatch(dbHistory, chartGoalDtos);
			ChartServiceHelper.updateOpponentForMatch(dbHistory, chartOpponentDtos);
			ChartServiceHelper.updatePointForMatch(dbHistory, chartPointDtos);
		}

		final ChartContainer container = new ChartContainer();
		container.getChartGameDtos().addAll(chartGameDtos.values());
		container.getChartGoalDtos().addAll(chartGoalDtos.values());
		container.getChartOpponentDtos().addAll(chartOpponentDtos.values());
		container.getChartPointDtos().addAll(chartPointDtos);

		return container;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InfoDto getDoublePlayerInfo(PlayerDto playerDto, Integer year) throws IllegalArgumentException {
		final List<DoubleMatchHistory> dbDoubleMatches = MatchBean.getDoubleMatchesForPlayer(playerDto, year);

		final Player dbPlayer = PMFactory.getObjectById(Player.class, playerDto.getId(), PlayerPlan.PLAYERSINGLESTATS);

		final InfoDto infoDto = new InfoDto();
		infoDto.setWinSeries(ChartServiceHelper.getMatchWinSeries(dbDoubleMatches));
		infoDto.setDefeatSeries(ChartServiceHelper.getMatchDefeatSeries(dbDoubleMatches));
		infoDto.setMaxWinPoints(ChartServiceHelper.getMatchMaxWinPoints(dbDoubleMatches));
		infoDto.setMaxLostPoints(ChartServiceHelper.getMatchMaxLostPoints(dbDoubleMatches));
		infoDto.setMaxPoints(ChartServiceHelper.getMatchMaxPoints(dbDoubleMatches));
		infoDto.setMinPoints(ChartServiceHelper.getMatchMinPoints(dbDoubleMatches));
		infoDto.setPercentageWins(ChartServiceHelper.getPercentageWins(dbPlayer));
		infoDto.setAveragePoints(ChartServiceHelper.getAveragePoints(dbDoubleMatches, dbPlayer));
		infoDto.setAverageTablePlace(ChartServiceHelper.getAverageTablePlace(dbDoubleMatches, dbPlayer));

		return infoDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChartContainer getDoublePlayerChart(PlayerDto playerDto, Date date) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

}
