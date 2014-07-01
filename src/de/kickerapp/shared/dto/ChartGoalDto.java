package de.kickerapp.shared.dto;

import de.kickerapp.shared.common.BaseDto;

public class ChartGoalDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -273481068953187968L;

	private String month;

	private Integer shotGoals;

	private Integer getGoals;

	public ChartGoalDto() {
		super();

		month = "";
		shotGoals = 0;
		getGoals = 0;
	}

	public ChartGoalDto(Long id, String month) {
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

}
