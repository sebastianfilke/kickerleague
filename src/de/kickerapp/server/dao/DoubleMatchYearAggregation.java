package de.kickerapp.server.dao;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.datanucleus.annotations.Unowned;

import de.kickerapp.server.dao.fetchplans.MatchAggregationPlan;

/**
 * Datenklasse zum Halten der Informationen für die Anzahl der Spiele eines Spielers für Doppelspiele.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
@FetchGroup(name = MatchAggregationPlan.PLAYER, members = { @Persistent(name = "player") })
public class DoubleMatchYearAggregation extends MatchYearAggregation {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1981220003150866036L;

	/** Der Spieler. */
	@Unowned
	@Persistent(defaultFetchGroup = "false")
	private Player player;

	/**
	 * Erzeugt eine neue Anzahl von Spielen ohne Angaben.
	 */
	public DoubleMatchYearAggregation() {
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
