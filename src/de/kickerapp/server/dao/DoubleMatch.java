package de.kickerapp.server.dao;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.datanucleus.annotations.Unowned;

/**
 * Datenklasse zum Halten der Informationen für ein Doppelspiel.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
public class DoubleMatch extends Match {

	/** Das erste Team. */
	@Unowned
	@Persistent(defaultFetchGroup = "true")
	private Team team1;
	/** Das zweite Team. */
	@Unowned
	@Persistent(defaultFetchGroup = "true")
	private Team team2;

	/**
	 * Erzeugt ein neues Doppelspiel ohne Angaben.
	 */
	public DoubleMatch() {
		super();
	}

	/**
	 * Liefert das erste Team.
	 * 
	 * @return Das erste Team als {@link Team}.
	 */
	public Team getTeam1() {
		return team1;
	}

	/**
	 * Setzt das erste Team.
	 * 
	 * @param team1 Das erste Team als {@link Team}.
	 */
	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	/**
	 * Liefert das zweite Team.
	 * 
	 * @return Das zweite Team als {@link Team}.
	 */
	public Team getTeam2() {
		return team2;
	}

	/**
	 * Setzt das zweite Team.
	 * 
	 * @param team2 Das zweite Team als {@link Team}.
	 */
	public void setTeam2(Team team2) {
		this.team2 = team2;
	}

}
