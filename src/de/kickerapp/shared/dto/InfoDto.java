package de.kickerapp.shared.dto;

import de.kickerapp.shared.common.BaseDto;

/**
 * Client-Datenklasse zum Halten des Spielverlaufs für ein Team bzw. Spieler.
 * 
 * @author Sebastian Filke
 */
public class InfoDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -4934809671424931355L;

	/** Die längste Gewinnserie des Team bzw. Spielers. */
	private Integer winSeries;
	/** Die längste Niederlagenserie des Team bzw. Spielers. */
	private Integer defeatSeries;
	/** Der höchste Punktegewinn des Team bzw. Spielers. */
	private Integer maxWinPoints;
	/** Der höchste Punkteverlust des Team bzw. Spielers. */
	private Integer maxLostPoints;
	/** Die höchste Punktezahl des Team bzw. Spielers. */
	private Integer maxPoints;
	/** Die niedrigste Punktezahl des Team bzw. Spielers. */
	private Integer minPoints;
	/** Die prozentualen Siege des Team bzw. Spielers. */
	private String percentageWins;
	/** Die durchschnittlichen Punkte des Team bzw. Spielers. */
	private String averagePoints;
	/** Der durchschnittliche Tabellenplatz des Team bzw. Spielers. */
	private String averageTablePlace;

	/**
	 * Erstellt einen neuen Spielverlauf ohne Angaben.
	 */
	public InfoDto() {
		super();

		winSeries = 0;
		defeatSeries = 0;
		maxWinPoints = 0;
		maxLostPoints = 0;
		maxPoints = 0;
		minPoints = 0;
		percentageWins = "0.0";
		averagePoints = "0.0";
		averageTablePlace = "0.0";
	}

	/**
	 * Liefert die längste Gewinnserie des Team bzw. Spielers.
	 * 
	 * @return Die längste Gewinnserie des Team bzw. Spielers als {@link Integer}.
	 */
	public Integer getWinSeries() {
		return winSeries;
	}

	/**
	 * Setzt die längste Gewinnserie des Team bzw. Spielers.
	 * 
	 * @param winSeries Die längste Gewinnserie des Team bzw. Spielers als {@link Integer}.
	 */
	public void setWinSeries(Integer winSeries) {
		this.winSeries = winSeries;
	}

	/**
	 * Liefert die längste Niederlagenserie des Team bzw. Spielers.
	 * 
	 * @return Die längste Niederlagenserie des Team bzw. Spielers als {@link Integer}.
	 */
	public Integer getDefeatSeries() {
		return defeatSeries;
	}

	/**
	 * Setzt die längste Niederlagenserie des Team bzw. Spielers.
	 * 
	 * @param defeatSeries Die längste Niederlagenserie des Team bzw. Spielers als {@link Integer}.
	 */
	public void setDefeatSeries(Integer defeatSeries) {
		this.defeatSeries = defeatSeries;
	}

	/**
	 * Liefert den höchsten Punktegewinn des Team bzw. Spielers.
	 * 
	 * @return Der höchste Punktegewinn des Team bzw. Spielers als {@link Integer}.
	 */
	public Integer getMaxWinPoints() {
		return maxWinPoints;
	}

	/**
	 * Setzt den höchsten Punktegewinn des Team bzw. Spielers.
	 * 
	 * @param maxWinPoints Der höchste Punktegewinn des Team bzw. Spielers als {@link Integer}.
	 */
	public void setMaxWinPoints(Integer maxWinPoints) {
		this.maxWinPoints = maxWinPoints;
	}

	/**
	 * Liefert den höchsten Punkteverlust des Team bzw. Spielers.
	 * 
	 * @return Der höchste Punkteverlust des Team bzw. Spielers als {@link Integer}.
	 */
	public Integer getMaxLostPoints() {
		return maxLostPoints;
	}

	/**
	 * Setzt den höchsten Punkteverlust des Team bzw. Spielers.
	 * 
	 * @param maxLostPoints Der höchste Punkteverlust des Team bzw. Spielers als {@link Integer}.
	 */
	public void setMaxLostPoints(Integer maxLostPoints) {
		this.maxLostPoints = maxLostPoints;
	}

	/**
	 * Liefert die höchste Punktezahl des Team bzw. Spielers.
	 * 
	 * @return Die höchste Punktezahl des Team bzw. Spielers als {@link Integer}.
	 */
	public Integer getMaxPoints() {
		return maxPoints;
	}

	/**
	 * Setzt die höchste Punktezahl des Team bzw. Spielers.
	 * 
	 * @param maxPoints Die höchste Punktezahl des Team bzw. Spielers als {@link Integer}.
	 */
	public void setMaxPoints(Integer maxPoints) {
		this.maxPoints = maxPoints;
	}

	/**
	 * Liefert die niedrigste Punktezahl des Team bzw. Spielers.
	 * 
	 * @return Die niedrigste Punktezahl des Team bzw. Spielers als {@link Integer}.
	 */
	public Integer getMinPoints() {
		return minPoints;
	}

	/**
	 * Setzt die niedrigste Punktezahl des Team bzw. Spielers.
	 * 
	 * @param minPoints Die niedrigste Punktezahl des Team bzw. Spielers als {@link Integer}.
	 */
	public void setMinPoints(Integer minPoints) {
		this.minPoints = minPoints;
	}

	/**
	 * Liefert die prozentualen Siege des Team bzw. Spielers.
	 * 
	 * @return Die prozentualen Siege des Team bzw. Spielers als {@link String}.
	 */
	public String getPercentageWins() {
		return percentageWins;
	}

	/**
	 * Setzt die prozentualen Siege des Team bzw. Spielers.
	 * 
	 * @param averageWins Die prozentualen Siege des Team bzw. Spielers als {@link String}.
	 */
	public void setPercentageWins(String averageWins) {
		this.percentageWins = averageWins;
	}

	/**
	 * Liefert die durchschnittlichen Punkte des Team bzw. Spielers.
	 * 
	 * @return Die durchschnittlichen Punkte des Team bzw. Spielers als {@link String}.
	 */
	public String getAveragePoints() {
		return averagePoints;
	}

	/**
	 * Setzt die durchschnittlichen Punkte des Team bzw. Spielers.
	 * 
	 * @param averagePoints Die durchschnittlichen Punkte des Team bzw. Spielers als {@link String}.
	 */
	public void setAveragePoints(String averagePoints) {
		this.averagePoints = averagePoints;
	}

	/**
	 * Liefert den durchschnittlichen Tabellenplatz des Team bzw. Spielers.
	 * 
	 * @return Der durchschnittlichen Tabellenplatz des Team bzw. Spielers als {@link String}.
	 */
	public String getAverageTablePlace() {
		return averageTablePlace;
	}

	/**
	 * Setzt den durchschnittlichen Tabellenplatz des Team bzw. Spielers.
	 * 
	 * @param averageTablePlace Der durchschnittlichen Tabellenplatz des Team bzw. Spielers als {@link String}.
	 */
	public void setAverageTablePlace(String averageTablePlace) {
		this.averageTablePlace = averageTablePlace;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(getId()).append(", ");
		sb.append("winSeries=").append(winSeries).append(", ");
		sb.append("defeatSeries=").append(defeatSeries).append(", ");
		sb.append("maxWinPoints=").append(maxWinPoints).append(", ");
		sb.append("maxLostPoints=").append(maxLostPoints).append(", ");
		sb.append("maxPoints=").append(maxPoints).append(", ");
		sb.append("minPoints=").append(minPoints).append(", ");
		sb.append("averageWins=").append(percentageWins).append(", ");
		sb.append("averagePoints=").append(averagePoints).append(", ");
		sb.append("averageTablePlace=").append(averageTablePlace);
		sb.append("]");

		return sb.toString();
	}

}
