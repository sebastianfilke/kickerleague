package de.kickerapp.server.entity;

import java.util.Date;
import java.util.HashSet;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Datenklasse zum Halten der Informationen für ein Team.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class Team extends BaseEntity {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 7796571017092493823L;

	/** Das Datum des letzten Spiels des Teams. */
	@Persistent
	private Date lastMatchDate;
	/** Die Teamspiel-Statistik des Teams. */
	@Persistent
	private Long teamStats;
	/** Die Spieler des Teams. */
	@Persistent
	private HashSet<Long> players;

	/**
	 * Erzeugt ein neues Team ohne Angaben und leeren Statistiken.
	 */
	public Team() {
		super();

		lastMatchDate = null;
		teamStats = 0L;
		players = new HashSet<Long>();
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
	 * @return Die Teamspiel-Statistik des Teams als {@link Long}.
	 */
	public Long getTeamStats() {
		return teamStats;
	}

	/**
	 * Setzt die Teamspiel-Statistik des Teams.
	 * 
	 * @param teamStats Die Teamspiel-Statistik des Teams als {@link Long}.
	 */
	public void setTeamStats(Long teamStats) {
		this.teamStats = teamStats;
	}

	/**
	 * Liefert die Spieler des Teams.
	 * 
	 * @return Die Spieler des Teams als {@link HashSet}.
	 */
	public HashSet<Long> getPlayers() {
		return players;
	}

	/**
	 * Setzt die Spieler des Teams.
	 * 
	 * @param players Die Spieler des Teams als {@link HashSet}.
	 */
	public void setPlayers(HashSet<Long> players) {
		this.players = players;
	}

}
