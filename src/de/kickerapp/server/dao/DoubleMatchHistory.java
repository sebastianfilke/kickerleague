package de.kickerapp.server.dao;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchGroups;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.datanucleus.annotations.Unowned;

import de.kickerapp.server.dao.fetchplans.MatchHistoryPlan;

/**
 * Datenklasse zum Halten der Informationen für den Verlauf eines Doppelspiels.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
@FetchGroups({
		@FetchGroup(name = MatchHistoryPlan.PLAYER1, members = { @Persistent(name = "player1") }),
		@FetchGroup(name = MatchHistoryPlan.PLAYER2, members = { @Persistent(name = "player2") }),
		@FetchGroup(name = MatchHistoryPlan.BOTHPLAYERS, members = { @Persistent(name = "player1"), @Persistent(name = "player2") }),
		@FetchGroup(name = MatchHistoryPlan.ALLPLAYERS, members = { @Persistent(name = "player1"), @Persistent(name = "player2"),
				@Persistent(name = "player3"), @Persistent(name = "player4") }) })
public class DoubleMatchHistory extends MatchHistory {

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
	/** Der dritte Spieler. */
	@Unowned
	@Persistent(defaultFetchGroup = "false")
	private Player player3;
	/** Der vierte Spieler. */
	@Unowned
	@Persistent(defaultFetchGroup = "false")
	private Player player4;

	/**
	 * Erzeugt einen neuen Verlauf für ein Doppelspiel ohne Angaben.
	 */
	public DoubleMatchHistory() {
		super();

		player1 = null;
		player2 = null;
		player3 = null;
		player4 = null;
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
	 * Liefert den dritten Spieler.
	 * 
	 * @return Der dritte Spieler als {@link Player}.
	 */
	public Player getPlayer3() {
		return player3;
	}

	/**
	 * Setzt den dritten Spieler.
	 * 
	 * @param player3 Der dritte Spieler als {@link Player}.
	 */
	public void setPlayer3(Player player3) {
		this.player3 = player3;
	}

	/**
	 * Liefert den vierten Spieler.
	 * 
	 * @return Der vierten Spieler als {@link Player}.
	 */
	public Player getPlayer4() {
		return player4;
	}

	/**
	 * Setzt den vierten Spieler.
	 * 
	 * @param player4 Der vierten Spieler als {@link Player}.
	 */
	public void setPlayer4(Player player4) {
		this.player4 = player4;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return super.toString();
	}

}
