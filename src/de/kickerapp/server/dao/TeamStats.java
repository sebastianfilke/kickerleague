package de.kickerapp.server.dao;

import javax.jdo.annotations.PersistenceCapable;

/**
 * Datenklasse zum Halten der Informationen für die Teamspiel-Statistik eines Teams.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
public class TeamStats extends Stats {

	/**
	 * Erzeugt eine leere Teamspiel-Statistik für ein Team.
	 */
	public TeamStats() {
		super();
	}

}
