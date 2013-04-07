package de.kickerapp.shared.dto;

import java.util.ArrayList;

import de.kickerapp.shared.common.BaseDto;

public class ChartDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -4934809671424931355L;

	private Integer winSeries;

	private Integer lossSeries;

	private Integer maxWinPoints;

	private Integer maxLossPoints;

	private Integer maxPoints;

	private Integer minPoints;

	private String averageWins;

	private String averagePoints;

	private ArrayList<ChartDataDto> chartDataDto;

	public ChartDto() {
		super();

		winSeries = 0;
		lossSeries = 0;
		maxWinPoints = 0;
		maxLossPoints = 0;
		maxPoints = 0;
		minPoints = 0;
		averageWins = "0.0";
		averagePoints = "0.0";
		chartDataDto = new ArrayList<ChartDataDto>();
	}

	public Integer getWinSeries() {
		return winSeries;
	}

	public void setWinSeries(Integer winSeries) {
		this.winSeries = winSeries;
	}

	public Integer getLossSeries() {
		return lossSeries;
	}

	public void setLossSeries(Integer lossSeries) {
		this.lossSeries = lossSeries;
	}

	public Integer getMaxWinPoints() {
		return maxWinPoints;
	}

	public void setMaxWinPoints(Integer maxWinPoints) {
		this.maxWinPoints = maxWinPoints;
	}

	public Integer getMaxLossPoints() {
		return maxLossPoints;
	}

	public void setMaxLossPoints(Integer maxLossPoints) {
		this.maxLossPoints = maxLossPoints;
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

	public ArrayList<ChartDataDto> getChartDataDto() {
		return chartDataDto;
	}

	public void setChartDataDto(ArrayList<ChartDataDto> chartDataDto) {
		this.chartDataDto = chartDataDto;
	}

}
