package de.kickerapp.shared.dto;

/**
 * Client-Datenklasse zum Halten der Torstatistiken für ein Team bzw. Spieler.
 * 
 * @author Sebastian Filke
 */
public class ChartGoalDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -273481068953187968L;

	/** Der Monat für die Torstatistik. */
	private String month;
	/** Die geschossenen Tore für den Monat. */
	private Integer shotGoals;
	/** Die kassierten Tore für den Monat. */
	private Integer getGoals;

	/**
	 * Erzeugt eine leere Torstatistik ohne Angaben.
	 */
	public ChartGoalDto() {
		super();

		month = "";
		shotGoals = 0;
		getGoals = 0;
	}

	/**
	 * Erzeugt eine Torstatistik mit ID und Monat.
	 * 
	 * @param id Die ID als {@link Long}.
	 * @param month Der Monat als {@link String}.
	 */
	public ChartGoalDto(Long id, String month) {
		this();

		setId(id);
		this.month = month;
	}

	/**
	 * Liefert den Monat für die Torstatistik.
	 * 
	 * @return Der Monat für die Torstatistik als {@link String}.
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * Setzt den Monat für die Torstatistik.
	 * 
	 * @param month Der Monat für die Torstatistik als {@link String}.
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * Liefert die geschossenen Tore für den Monat.
	 * 
	 * @return Die geschossenen Tore für den Monat als {@link Integer}.
	 */
	public Integer getShotGoals() {
		return shotGoals;
	}

	/**
	 * Setzt die geschossenen Tore für den Monat.
	 * 
	 * @param shotGoals wins Die geschossenen Tore für den Monat als {@link Integer}.
	 */
	public void setShotGoals(Integer shotGoals) {
		this.shotGoals = shotGoals;
	}

	/**
	 * Liefert die kassierten Tore für den Monat.
	 * 
	 * @return Die kassierten Tore für den Monat als {@link Integer}.
	 */
	public Integer getGetGoals() {
		return getGoals;
	}

	/**
	 * Setzt die kassierten Tore für den Monat.
	 * 
	 * @param getGoals wins Die kassierten Tore für den Monat als {@link Integer}.
	 */
	public void setGetGoals(Integer getGoals) {
		this.getGoals = getGoals;
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
		sb.append("shotGoals=").append(shotGoals).append(", ");
		sb.append("getGoals=").append(getGoals);
		sb.append("]");

		return sb.toString();
	}

}
