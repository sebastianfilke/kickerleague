package de.kickerapp.shared.dto;

/**
 * Client-Datenklasse zum Halten der Spielstatistiken für ein Team bzw. Spieler.
 * 
 * @author Sebastian Filke
 */
public class ChartGameDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Der Monat für die Spielstatistik. */
	private String month;
	/** Die gewonnen Spiele für den Monat. */
	private Integer wins;
	/** Die verlorenen Spiele für den Monat. */
	private Integer defeats;

	/**
	 * Erzeugt eine leere Spielstatistik ohne Angaben.
	 */
	public ChartGameDto() {
		super();

		month = "";
		wins = 0;
		defeats = 0;
	}

	/**
	 * Erzeugt eine Spielstatistik mit ID und Monat.
	 * 
	 * @param id Die ID als {@link Long}.
	 * @param month Der Monat als {@link String}.
	 */
	public ChartGameDto(Long id, String month) {
		this();

		setId(id);
		this.month = month;
	}

	/**
	 * Liefert den Monat für die Spielstatistik.
	 * 
	 * @return Der Monat für die Spielstatistik als {@link String}.
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * Setzt den Monat für die Spielstatistik.
	 * 
	 * @param month Der Monat für die Spielstatistik als {@link String}.
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * Liefert die gewonnen Spiele für den Monat.
	 * 
	 * @return Die gewonnen Spiele für den Monat als {@link Integer}.
	 */
	public Integer getWins() {
		return wins;
	}

	/**
	 * Setzt die gewonnen Spiele für den Monat.
	 * 
	 * @param wins Die gewonnen Spiele für den Monat als {@link Integer}.
	 */
	public void setWins(Integer wins) {
		this.wins = wins;
	}

	/**
	 * Liefert die verlorenen Spiele für den Monat.
	 * 
	 * @return Die verlorenen Spiele für den Monat als {@link Integer}.
	 */
	public Integer getDefeats() {
		return defeats;
	}

	/**
	 * Setzt die verlorenen Spiele für den Monat.
	 * 
	 * @param defeats Die verlorenen Spiele für den Monat als {@link Integer}.
	 */
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
