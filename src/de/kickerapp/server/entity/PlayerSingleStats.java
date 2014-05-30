package de.kickerapp.server.entity;

import javax.jdo.annotations.PersistenceCapable;

/**
 * Datenklasse zum Halten der Informationen für die Einzelspiel-Statistik eines Spielers.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class PlayerSingleStats extends Stats {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -6517748304014163638L;

	/**
	 * Erzeugt eine leeren Einzelspiel-Statistik für einen Spieler.
	 */
	public PlayerSingleStats() {
		super();
	}

}
