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
	private String averageWins;
	/** Der Punktedurchschnitt des Team bzw. Spielers. */
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
		averageWins = "0.0";
		averagePoints = "0.0";
		averageTablePlace = "0.0";
	}

	public Integer getWinSeries() {
		return winSeries;
	}

	public void setWinSeries(Integer winSeries) {
		this.winSeries = winSeries;
	}

	public Integer getDefeatSeries() {
		return defeatSeries;
	}

	public void setDefeatSeries(Integer defeatSeries) {
		this.defeatSeries = defeatSeries;
	}

	public Integer getMaxWinPoints() {
		return maxWinPoints;
	}

	public void setMaxWinPoints(Integer maxWinPoints) {
		this.maxWinPoints = maxWinPoints;
	}

	public Integer getMaxLostPoints() {
		return maxLostPoints;
	}

	public void setMaxLostPoints(Integer maxLostPoints) {
		this.maxLostPoints = maxLostPoints;
	}

	public Integer getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(Integer maxPoints) {
		this.maxPoints = maxPoints;
	}

	public Integer getMinPoints() {
		return minPoints;
	}

	public void setMinPoints(Integer minPoints) {
		this.minPoints = minPoints;
	}

	public String getAverageWins() {
		return averageWins;
	}

	public void setAverageWins(String averageWins) {
		this.averageWins = averageWins;
	}

	public String getAveragePoints() {
		return averagePoints;
	}

	public void setAveragePoints(String averagePoints) {
		this.averagePoints = averagePoints;
	}

	public String getAverageTablePlace() {
		return averageTablePlace;
	}

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
		sb.append("averageWins=").append(averageWins).append(", ");
		sb.append("averagePoints=").append(averagePoints).append(", ");
		sb.append("averageTablePlace=").append(averageTablePlace);
		sb.append("]");

		return sb.toString();
	}

}
