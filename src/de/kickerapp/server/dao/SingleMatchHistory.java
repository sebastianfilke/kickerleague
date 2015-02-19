package de.kickerapp.server.dao;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchGroups;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.datanucleus.annotations.Unowned;

import de.kickerapp.server.dao.fetchplans.MatchHistoryPlan;

/**
 * Datenklasse zum Halten der Informationen für den Verlauf eines Einzelspiels.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
@FetchGroups({ @FetchGroup(name = MatchHistoryPlan.PLAYER1, members = { @Persistent(name = "player1") }),
		@FetchGroup(name = MatchHistoryPlan.PLAYER2, members = { @Persistent(name = "player2") }),
		@FetchGroup(name = MatchHistoryPlan.BOTHPLAYERS, members = { @Persistent(name = "player1"), @Persistent(name = "player2") }) })
public class SingleMatchHistory extends MatchHistory {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Der erste Spieler. */
	@Unowned
	@Persistent(defaultFetchGroup = "false")
	private Player player1;
	/** Der zweite Spieler. */
	@Unowned
	@Persistent(defaultFetchGroup = "false")
	private Player player2;

	/**
	 * Erzeugt einen neuen Verlauf für ein Einzelspiel ohne Angaben.
	 */
	public SingleMatchHistory() {
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
	 * @return Der zweite Spieler als {@link Player}.
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return super.toString();
	}

}
