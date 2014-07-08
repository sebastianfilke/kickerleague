package de.kickerapp.shared.dto;

/**
 * Client-Datenklasse zum Halten der Informationen für die Einzelspiel-Statistik eines Spielers.
 * 
 * @author Sebastian Filke
 */
public class PlayerSingleStatsDto extends StatsDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -5045714917141004133L;

	/**
	 * Erzeugt eine leeren Einzelspiel-Statistik für einen Spieler.
	 */
	public PlayerSingleStatsDto() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return super.toString();
	}

}
