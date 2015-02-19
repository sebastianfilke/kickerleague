package de.kickerapp.server.dao;

import javax.jdo.annotations.PersistenceCapable;

/**
 * Datenklasse zum Halten der Informationen für die Doppelspiel-Statistik eines Spielers.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
public class PlayerDoubleStats extends Stats {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Erzeugt eine leere Doppelspiel-Statistik für einen Spieler.
	 */
	public PlayerDoubleStats() {
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
