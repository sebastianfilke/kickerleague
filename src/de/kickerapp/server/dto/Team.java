package de.kickerapp.server.dto;

import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Datenklasse zum Halten der Informationen für einen Team.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class Team extends BaseEntity {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 7796571017092493823L;

	@Persistent
	private Player player;

	@Persistent(mappedBy = "team")
	private ArrayList<Set> sets;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ArrayList<Set> getSets() {
		return sets;
	}

	public void setSets(ArrayList<Set> sets) {
		this.sets = sets;
	}

}
