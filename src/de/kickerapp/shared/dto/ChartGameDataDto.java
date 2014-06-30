package de.kickerapp.shared.dto;


public class ChartGameDataDto extends ChartDataDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 6678498198409457122L;

	private Integer wins;

	private Integer defeats;

	public ChartGameDataDto() {
		super();

		wins = 0;
		defeats = 0;
	}

	public ChartGameDataDto(Long id, String month) {
		super(id, month);
		wins = 0;
		defeats = 0;
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
