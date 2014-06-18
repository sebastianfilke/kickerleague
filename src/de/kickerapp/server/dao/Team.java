package de.kickerapp.server.dao;

import java.util.ArrayList;
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
		@FetchGroup(name = TeamPlan.PLAYERS, members = { @Persistent(name = "players") }) })
public class Team extends BaseDao {

	/** Das Datum des letzten Spiels des Teams. */
	@Persistent
	private Date lastMatchDate;
	/** Die Teamspiel-Statistik des Teams. */
	@Element(dependent = "true")
	@Persistent(defaultFetchGroup = "false")
	private TeamStats teamStats;
	/** Die Spieler des Teams. */
	@Unowned
	@Persistent(defaultFetchGroup = "false")
	private ArrayList<Player> players;

	/**
	 * Erzeugt ein neues Team ohne Angaben und leeren Statistiken.
	 */
	public Team() {
		super();

		lastMatchDate = null;
		teamStats = null;
		players = new ArrayList<Player>();
	}

	/**
	 * Erzeugt ein neues Team mit den übergebenen Spielern.
	 * 
	 * @param player1 Der erste Spieler des Teams als {@link Player}.
	 * @param player2 Der zweite Spieler des Teams als {@link Player}.
	 */
	public Team(Player player1, Player player2) {
		this();
		players.add(player1);
		players.add(player2);
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
	 * Liefert die Spieler des Teams.
	 * 
	 * @return Die Spieler des Teams als {@link ArrayList}.
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}

	/**
	 * Setzt die Spieler des Teams.
	 * 
	 * @param players Die Spieler des Teams als {@link ArrayList}.
	 */
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getName());
		sb.append(" [");
		sb.append("id=").append(getKey().getId()).append(", ");
		sb.append("]");

		return sb.toString();
	}

}
