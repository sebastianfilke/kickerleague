package de.kickerapp.server.entity;

import javax.jdo.annotations.PersistenceCapable;

/**
 * Datenklasse zum Halten der Informationen für die Teamspiel-Statistik eines Teams.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class TeamStats extends Stats {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -3062855558058670180L;

	/**
	 * Erzeugt eine leere Teamspiel-Statistik für ein Team.
	 */
	public TeamStats() {
		super();
	}

}
