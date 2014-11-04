package de.kickerapp.shared.container;

import java.util.ArrayList;

import de.kickerapp.shared.common.BaseSerializable;
import de.kickerapp.shared.dto.ChartGameDto;
import de.kickerapp.shared.dto.ChartGoalDto;
import de.kickerapp.shared.dto.ChartOpponentDto;
import de.kickerapp.shared.dto.ChartPointDto;

public class ChartContainer implements BaseSerializable {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -6466711357162157543L;

	private ArrayList<ChartGameDto> chartGameDtos;

	private ArrayList<ChartGoalDto> chartGoalDtos;

	private ArrayList<ChartPointDto> chartPointDtos;

	private ArrayList<ChartOpponentDto> chartOpponentDtos;

	public ChartContainer() {
		super();

		chartGameDtos = new ArrayList<ChartGameDto>();
		chartGoalDtos = new ArrayList<ChartGoalDto>();
		chartPointDtos = new ArrayList<ChartPointDto>();
		chartOpponentDtos = new ArrayList<ChartOpponentDto>();
	}

	public ArrayList<ChartGameDto> getChartGameDtos() {
		return chartGameDtos;
	}

	public void setChartGameDtos(ArrayList<ChartGameDto> chartGameDtos) {
		this.chartGameDtos = chartGameDtos;
	}

	public ArrayList<ChartGoalDto> getChartGoalDtos() {
		return chartGoalDtos;
	}

	public void setChartGoalDtos(ArrayList<ChartGoalDto> chartGoalDtos) {
		this.chartGoalDtos = chartGoalDtos;
	}

	public ArrayList<ChartPointDto> getChartPointDtos() {
		return chartPointDtos;
	}

	public void setChartPointDtos(ArrayList<ChartPointDto> chartPointDtos) {
		this.chartPointDtos = chartPointDtos;
	}

	public ArrayList<ChartOpponentDto> getChartOpponentDtos() {
		return chartOpponentDtos;
	}

	public void setChartOpponentDtos(ArrayList<ChartOpponentDto> chartOpponentDtos) {
		this.chartOpponentDtos = chartOpponentDtos;
	}

}
