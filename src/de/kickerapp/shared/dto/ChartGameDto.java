package de.kickerapp.shared.dto;

import de.kickerapp.shared.common.BaseDto;

public class ChartGameDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -1759614740386503908L;

	private String month;

	private Integer wins;

	private Integer defeats;

	public ChartGameDto() {
		super();

		month = "";
		wins = 0;
		defeats = 0;
	}

	public ChartGameDto(Long id, String month) {
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
