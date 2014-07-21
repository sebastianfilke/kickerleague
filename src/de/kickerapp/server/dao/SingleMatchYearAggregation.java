package de.kickerapp.server.dao;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.datanucleus.annotations.Unowned;

/**
 * Datenklasse zum Halten der Informationen für die Anzahl der Spiele eines Spielers für Einzelspiele.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
public class SingleMatchYearAggregation extends MatchYearAggregation {

	/** Der Spieler. */
	@Unowned
	@Persistent(defaultFetchGroup = "false")
	private Player player;

	/**
	 * Erzeugt eine neue Anzahl von Spielen ohne Angaben.
	 */
	public SingleMatchYearAggregation() {
		super();

		player = null;
	}

	/**
	 * Liefert den Spieler.
	 * 
	 * @return Der Spieler als {@link Player}.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Setzt den Spieler.
	 * 
	 * @param player Der Spieler als {@link Player}.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return super.toString();
	}

}
