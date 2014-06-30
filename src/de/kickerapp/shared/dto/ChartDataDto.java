package de.kickerapp.shared.dto;

import de.kickerapp.shared.common.BaseDto;

public class ChartDataDto extends BaseDto {

	private static final long serialVersionUID = -1759614740386503908L;

	private String month;

	public ChartDataDto() {
		super();

		month = "";
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

}
