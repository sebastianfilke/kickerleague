package de.kickerapp.server.dao;

import java.util.Date;
import java.util.TreeSet;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import de.kickerapp.server.dao.fetchplans.TeamPlan;

/**
 * Datenklasse zum Halten der Informationen für ein Team.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
@FetchGroup(name = TeamPlan.TEAMSTATS, members = { @Persistent(name = "teamStats") })
public class Team extends BaseDao {

	/** Das Datum des letzten Spiels des Teams. */
	@Persistent
	private Date lastMatchDate;
	/** Die Teamspiel-Statistik des Teams. */
	@Element(dependent = "true")
	@Persistent(defaultFetchGroup = "false")
	private TeamStats teamStats;
	/** Die Spieler des Teams. */
	@Persistent
	private TreeSet<Long> players;

	/**
	 * Erzeugt ein neues Team ohne Angaben und leeren Statistiken.
	 */
	public Team() {
		super();

		lastMatchDate = null;
		teamStats = null;
		players = new TreeSet<Long>();
	}

	/**
	 * Erzeugt ein neues Team mit den übergebenen Spielern.
	 * 
	 * @param player1 Der erste Spieler des Teams als {@link Player}.
	 * @param player2 Der zweite Spieler des Teams als {@link Player}.
	 */
	public Team(Player player1, Player player2) {
		this();
		players.add(player1.getKey().getId());
		players.add(player2.getKey().getId());
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
	 * @return Die Spieler des Teams als {@link TreeSet}.
	 */
	public TreeSet<Long> getPlayers() {
		return players;
	}

	/**
	 * Setzt die Spieler des Teams.
	 * 
	 * @param players Die Spieler des Teams als {@link TreeSet}.
	 */
	public void setPlayers(TreeSet<Long> players) {
		this.players = players;
	}

}
