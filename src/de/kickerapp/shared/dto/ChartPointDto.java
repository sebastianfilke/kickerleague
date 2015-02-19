package de.kickerapp.shared.dto;

/**
 * Client-Datenklasse zum Halten der Punktestatistik für ein Team bzw. Spieler.
 * 
 * @author Sebastian Filke
 */
public class ChartPointDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Die Spielnummer. */
	private Integer matchNumber;
	/** Die Punkte des Teams bzw. Spielers. */
	private Integer points;

	/**
	 * Erzeugt eine leere Punktestatistik.
	 */
	public ChartPointDto() {
		super();

		matchNumber = 0;
		points = 0;
	}

	/**
	 * Liefert die Spielnummer.
	 * 
	 * @return Die Spielnummer als {@link Integer}.
	 */
	public Integer getMatchNumber() {
		return matchNumber;
	}

	/**
	 * Setzt die Spielnummer.
	 * 
	 * @param matchNumber Die Spielnummer als {@link Integer}.
	 */
	public void setMatchNumber(Integer matchNumber) {
		this.matchNumber = matchNumber;
	}

	/**
	 * Liefert die Punkte des Teams bzw. Spielers.
	 * 
	 * @return Die Punkte des Teams bzw. Spielers {@link Integer}.
	 */
	public Integer getPoints() {
		return points;
	}

	/**
	 * Setzt die Punkte des Teams bzw. Spielers.
	 * 
	 * @param points Die Punkte des Teams bzw. Spielers {@link Integer}.
	 */
	public void setPoints(Integer points) {
		this.points = points;
	}

}
