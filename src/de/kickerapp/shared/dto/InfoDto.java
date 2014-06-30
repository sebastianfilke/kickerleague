package de.kickerapp.shared.dto;

import java.util.ArrayList;

import de.kickerapp.shared.common.BaseDto;

public class InfoDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -4934809671424931355L;

	private Integer winSeries;

	private Integer defeatSeries;

	private Integer maxWinPoints;

	private Integer maxLostPoints;

	private Integer maxPoints;

	private Integer minPoints;

	private String averageWins;

	private String averagePoints;

	private ArrayList<ChartGameDataDto> chartDataDto;

	public InfoDto() {
		super();

		winSeries = 0;
		defeatSeries = 0;
		maxWinPoints = 0;
		maxLostPoints = 0;
		maxPoints = 0;
		minPoints = 0;
		averageWins = "0.0";
		averagePoints = "0.0";
		chartDataDto = new ArrayList<ChartGameDataDto>();
	}

	public Integer getWinSeries() {
		return winSeries;
	}

	public void setWinSeries(Integer winSeries) {
		this.winSeries = winSeries;
	}

	public Integer getDefeatSeries() {
		return defeatSeries;
	}

	public void setDefeatSeries(Integer defeatSeries) {
		this.defeatSeries = defeatSeries;
	}

	public Integer getMaxWinPoints() {
		return maxWinPoints;
	}

	public void setMaxWinPoints(Integer maxWinPoints) {
		this.maxWinPoints = maxWinPoints;
	}

	public Integer getMaxLostPoints() {
		return maxLostPoints;
	}

	public void setMaxLostPoints(Integer maxLostPoints) {
		this.maxLostPoints = maxLostPoints;
	}

	public Integer getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(Integer maxPoints) {
		this.maxPoints = maxPoints;
	}

	public Integer getMinPoints() {
		return minPoints;
	}

	public void setMinPoints(Integer minPoints) {
		this.minPoints = minPoints;
	}

	public String getAverageWins() {
		return averageWins;
	}

	public void setAverageWins(String averageWins) {
		this.averageWins = averageWins;
	}

	public String getAveragePoints() {
		return averagePoints;
	}

	public void setAveragePoints(String averagePoints) {
		this.averagePoints = averagePoints;
	}

	public ArrayList<ChartGameDataDto> getChartDataDto() {
		return chartDataDto;
	}

	public void setChartDataDto(ArrayList<ChartGameDataDto> chartDataDto) {
		this.chartDataDto = chartDataDto;
	}

}
