package de.kickerapp.server.dao;

import javax.jdo.annotations.PersistenceCapable;

/**
 * Datenklasse zum Halten der Informationen für die Einzelspiel-Statistik eines Spielers.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
public class PlayerSingleStats extends Stats {

	/**
	 * Erzeugt eine leeren Einzelspiel-Statistik für einen Spieler.
	 */
	public PlayerSingleStats() {
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
