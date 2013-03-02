package de.kickerapp.server.entity;

import javax.jdo.annotations.PersistenceCapable;

/**
 * Datenklasse zum Halten der Informationen für die Doppelspiel-Statistik eines Spielers.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class PlayerDoubleStats extends Stats {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -7869682689379852096L;

	/**
	 * Erzeugt eine leere Doppelspiel-Statistik für einen Spieler.
	 */
	public PlayerDoubleStats() {
		super();
	}

}
