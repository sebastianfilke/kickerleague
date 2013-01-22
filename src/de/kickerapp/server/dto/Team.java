package de.kickerapp.server.dto;

import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;

/**
 * Datenklasse zum Halten der Informationen für einen Team.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class Team extends BaseEntity {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 7796571017092493823L;

	/** Der Spieler des Teams. */
	private ArrayList<Long> players;

	/**
	 * Erzeugt ein neues Team.
	 */
	public Team() {
		super();
		players = new ArrayList<Long>();
	}

	public ArrayList<Long> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Long> players) {
		this.players = players;
	}

}
