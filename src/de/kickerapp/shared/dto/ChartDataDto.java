package de.kickerapp.shared.dto;

import de.kickerapp.shared.common.BaseDto;

public class ChartDataDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 6112150253103851416L;

	private String month;

	private Integer shotGoals;

	private Integer getGoals;

	private Integer wins;

	private Integer defeats;

	public ChartDataDto() {
		super();

		month = "";
		shotGoals = 0;
		getGoals = 0;
		wins = 0;
		defeats = 0;
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

	public Integer getDefeats() {
		return defeats;
	}

	public void setDefeats(Integer defeats) {
		this.defeats = defeats;
	}

}
