package de.kickerapp.shared.dto;

import de.kickerapp.shared.common.BaseDto;

/**
 * Client-Datenklasse zum Halten der Torstatistiken für ein Team bzw. Spieler.
 * 
 * @author Sebastian Filke
 */
public class ChartGameDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -1759614740386503908L;

	/** Der Monat für die Statistik. */
	private String month;

	private Integer wins;

	private Integer defeats;

	/**
	 * Erzeugt eine leere Torstatistik ohne Angaben.
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(getId()).append(", ");
		sb.append("month=").append(month).append(", ");
		sb.append("wins=").append(wins).append(", ");
		sb.append("defeats=").append(defeats);
		sb.append("]");

		return sb.toString();
	}

}
