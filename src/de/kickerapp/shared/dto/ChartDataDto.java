package de.kickerapp.shared.dto;

import de.kickerapp.shared.common.BaseDto;

public class ChartDataDto extends BaseDto {

	private String month;

	private Integer shotGoals;

	private Integer getGoals;

	private Integer wins;

	private Integer losses;

	public ChartDataDto() {
		super();

		month = "";
		shotGoals = 0;
		getGoals = 0;
		wins = 0;
		losses = 0;
	}

	public ChartDataDto(Long id, String month) {
		this();

		setId(id);
		this.month = month;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getShotGoals() {
		return shotGoals;
	}

	public void setShotGoals(Integer shotGoals) {
		this.shotGoals = shotGoals;
	}

	public Integer getGetGoals() {
		return getGoals;
	}

	public void setGetGoals(Integer getGoals) {
		this.getGoals = getGoals;
	}

	public Integer getWins() {
		return wins;
	}

	public void setWins(Integer wins) {
		this.wins = wins;
	}

	public Integer getLosses() {
		return losses;
	}

	public void setLosses(Integer losses) {
		this.losses = losses;
	}

}
