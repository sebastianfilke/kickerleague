package de.kickerapp.server.dao;

import javax.jdo.annotations.PersistenceCapable;

/**
 * Datenklasse zum Halten der Informationen für die Doppelspiel-Statistik eines Spielers.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class PlayerDoubleStats extends Stats {

	/**
	 * Erzeugt eine leere Doppelspiel-Statistik für einen Spieler.
	 */
	public PlayerDoubleStats() {
		super();
	}

}
