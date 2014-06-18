package de.kickerapp.server.dao;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.datanucleus.annotations.Unowned;

/**
 * Datenklasse zum Halten der Informationen für ein Einzelspiel.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
public class SingleMatch extends Match {

	/** Der erste Spieler. */
	@Unowned
	@Persistent(defaultFetchGroup = "true")
	private Player player1;
	/** Der zweite Spieler. */
	@Unowned
	@Persistent(defaultFetchGroup = "true")
	private Player player2;

	/**
	 * Erzeugt ein neues Einzelspiel ohne Angaben.
	 */
	public SingleMatch() {
		super();

		player1 = null;
		player2 = null;
	}

	/**
	 * Liefert den ersten Spieler.
	 * 
	 * @return Der erste Spieler als {@link Player}.
	 */
	public Player getPlayer1() {
		return player1;
	}

	/**
	 * Setzt den ersten Spieler.
	 * 
	 * @param player1 Der erste Spieler als {@link Player}.
	 */
	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	/**
	 * Liefert den zweiten Spieler.
	 * 
	 * @return Der zweite Spieler als {@link BaseDao}.
	 */
	public Player getPlayer2() {
		return player2;
	}

	/**
	 * Setzt den zweiten Spieler.
	 * 
	 * @param player2 Der zweite Spieler als {@link Player}.
	 */
	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

}
