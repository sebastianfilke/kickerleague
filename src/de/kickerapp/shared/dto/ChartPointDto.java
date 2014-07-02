package de.kickerapp.shared.dto;

import de.kickerapp.shared.common.BaseDto;

public class ChartPointDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -6317412684386825279L;

	private Integer matchNumber;

	private Integer points;

	public ChartPointDto() {
		super();

		matchNumber = 0;
		points = 0;
	}

	public Integer getMatchNumber() {
		return matchNumber;
	}

	public void setMatchNumber(Integer matchNumber) {
		this.matchNumber = matchNumber;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

}
