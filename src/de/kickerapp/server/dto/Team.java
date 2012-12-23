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
public class Team extends BaseDtoEntity {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 7796571017092493823L;

	/** Der Spieler des Teams. */
	@Persistent
	private Player player;
	/** Die Sätze des Spiels. */
	@Persistent(mappedBy = "team")
	private ArrayList<Set> sets;

	/**
	 * Setzt den Spieler des Teams.
	 * 
	 * @param player Der Spieler.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Liefert den Spieler des Teams.
	 * 
	 * @return Das Spieler des Teams.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Setzt die Sätze des Spiels.
	 * 
	 * @param sets Die Sätze.
	 */
	public void setSets(ArrayList<Set> sets) {
		this.sets = sets;
	}

	/**
	 * Liefert das Sätze des Spiels.
	 * 
	 * @return Das Sätze des Spiels.
	 */
	public ArrayList<Set> getSets() {
		return sets;
	}

}
