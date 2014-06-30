package de.kickerapp.shared.dto;

public class ChartGoalDataDto extends ChartDataDto {

	private Integer shotGoals;

	private Integer getGoals;

	public ChartGoalDataDto() {
		super();

		shotGoals = 0;
		getGoals = 0;
	}

	public ChartGoalDataDto(Long id, String month) {
		super(id, month);
		shotGoals = 0;
		getGoals = 0;
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
