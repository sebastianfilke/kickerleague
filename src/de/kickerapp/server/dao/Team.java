package de.kickerapp.server.dao;

import java.util.Date;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchGroups;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.datanucleus.annotations.Unowned;

import de.kickerapp.server.dao.fetchplans.TeamPlan;

/**
 * Datenklasse zum Halten der Informationen für ein Team.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
@FetchGroups({ @FetchGroup(name = TeamPlan.TEAMSTATS, members = { @Persistent(name = "teamStats") }),
		@FetchGroup(name = TeamPlan.BOTHPLAYERS, members = { @Persistent(name = "player1"), @Persistent(name = "player2") }) })
public class Team extends BaseDao {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Das Datum des letzten Spiels des Teams. */
	@Persistent
	private Date lastMatchDate;
	/** Die Teamspiel-Statistik des Teams. */
	@Element(dependent = "true")
	@Persistent(defaultFetchGroup = "false")
	private TeamStats teamStats;
	/** Der erste Spieler des Teams. */
	@Unowned
	@Persistent(defaultFetchGroup = "false")
	private Player player1;
	/** Der zweite Spieler des Teams. */
	@Unowned
	@Persistent(defaultFetchGroup = "false")
	private Player player2;

	/**
	 * Erzeugt ein neues Team ohne Angaben und leeren Statistiken.
	 */
	public Team() {
		super();

		lastMatchDate = null;
		teamStats = null;
		player1 = null;
		player2 = null;
	}

	/**
	 * Erzeugt ein neues Team mit den übergebenen Spielern.
	 * 
	 * @param player1 Der erste Spieler des Teams als {@link Player}.
	 * @param player2 Der zweite Spieler des Teams als {@link Player}.
	 */
	public Team(Player player1, Player player2) {
		this();
		this.player1 = player1;
		this.player2 = player2;
	}

	/**
	 * Liefert das Datum des letzten Spiels des Teams.
	 * 
	 * @return Das Datum des letzten Spiels des Teams als {@link Date}.
	 */
	public Date getLastMatchDate() {
		return lastMatchDate;
	}

	/**
	 * Setzt das Datum des letzten Spiels des Teams.
	 * 
	 * @param lastMatchDate Das Datum des letzten Spiels des Teams als {@link Date}.
	 */
	public void setLastMatchDate(Date lastMatchDate) {
		this.lastMatchDate = lastMatchDate;
	}

	/**
	 * Liefert die Teamspiel-Statistik des Teams.
	 * 
	 * @return Die Teamspiel-Statistik des Teams als {@link TeamStats}.
	 */
	public TeamStats getTeamStats() {
		return teamStats;
	}

	/**
	 * Setzt die Teamspiel-Statistik des Teams.
	 * 
	 * @param teamStats Die Teamspiel-Statistik des Teams als {@link TeamStats}.
	 */
	public void setTeamStats(TeamStats teamStats) {
		this.teamStats = teamStats;
	}

	/**
	 * Liefert den ersten Spieler des Teams.
	 * 
	 * @return Der erste Spieler des Teams als {@link Player}.
	 */
	public Player getPlayer1() {
		return player1;
	}

	/**
	 * Setzt den ersten Spieler des Teams.
	 * 
	 * @param player1 Der erste Spieler des Teams als {@link Player}.
	 */
	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	/**
	 * Liefert den zweiten Spieler des Teams.
	 * 
	 * @return Der zweite Spieler des Teams als {@link Player}.
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
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(getKey().getId()).append(", ");
		sb.append("lastMatchDate=").append(lastMatchDate);
		sb.append("]");

		return sb.toString();
	}

}
