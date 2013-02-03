package de.kickerapp.server.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

	/** Die Spieler des Teams. */
	@Persistent
	private Set<Long> players;
	/** Das Datum des letzten Spiels des Teams. */
	@Persistent
	private Date lastMatchDate;
	/** Die Statistik des Teams. */
	@Persistent
	private Long teamStats;

	/**
	 * Erzeugt ein neues Team.
	 */
	public Team() {
		super();
		players = new HashSet<Long>();
		lastMatchDate = null;
		teamStats = 0L;
	}

	public Team(Player player1, Player player2) {
		this();
		players.add(player1.getKey().getId());
		players.add(player2.getKey().getId());
	}

	public Set<Long> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Long> players) {
		this.players = players;
	}

	/**
	 * Setzt das Datum des letzten Spiels des Spielers.
	 * 
	 * @param lastMatchDate Das Datum des letzten Spiels des Spielers als
	 *            {@link Date}.
	 */
	public void setLastMatchDate(Date lastMatchDate) {
		this.lastMatchDate = lastMatchDate;
	}

	/**
	 * Liefert das Datum des letzten Spiels des Spielers.
	 * 
	 * @return Das Datum des letzten Spiels des Spielers als {@link Date}.
	 */
	public Date getLastMatchDate() {
		return lastMatchDate;
	}

	public Long getTeamStats() {
		return teamStats;
	}

	public void setTeamStats(Long teamStats) {
		this.teamStats = teamStats;
	}

}
