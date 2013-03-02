package de.kickerapp.server.entity;

import java.util.Date;
import java.util.HashSet;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Datenklasse zum Halten der Informationen für einen Spieler.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class Player extends BaseEntity {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -13034504212792103L;

	/** Der Nachname des Spielers. */
	@Persistent
	private String lastName;
	/** Der Vorname des Spielers. */
	@Persistent
	private String firstName;
	/** Der Nickname des Spielers. */
	@Persistent
	private String nickName;
	/** Die E-Mail Adresse des Spielers. */
	@Persistent
	private String eMail;
	/** Das Datum des letzten Spiels des Spielers. */
	@Persistent
	private Date lastMatchDate;
	/** Die Einzelspiel-Statistik des Spielers. */
	@Persistent
	private Long playerSingleStats;
	/** Die Doppelspiel-Statistik des Spielers. */
	@Persistent
	private Long playerDoubleStats;
	/** Die Liste der Teams zu denen der Spieler gehört. */
	@Persistent
	private HashSet<Long> teams;

	/**
	 * Erzeugt einen neuen Spieler ohne Angaben und leeren Statistiken.
	 */
	public Player() {
		super();

		lastName = "";
		firstName = "";
		nickName = "";
		eMail = "";
		lastMatchDate = null;
		playerSingleStats = 0L;
		playerDoubleStats = 0L;
		teams = new HashSet<Long>();
	}

	/**
	 * Erzeugt einen neuen Spieler mit Vor- und Nachnamen.
	 * 
	 * @param firstName Der Vornamen des Spielers als {@link String}.
	 * @param lastName Der Nachname des Spielers als {@link String}.
	 */
	public Player(String lastName, String firstName) {
		this();
		this.lastName = lastName;
		this.firstName = firstName;
	}

	/**
	 * Liefert den Nachnamen des Spielers.
	 * 
	 * @return Der Nachname des Spielers als {@link String}.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Setzt den Nachnamen des Spielers.
	 * 
	 * @param lastName Der Nachname des Spielers als {@link String}.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Liefert den Vornamen des Spielers.
	 * 
	 * @return Der Vornamen des Spielers als {@link String}.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Setzt den Vornamen des Spielers.
	 * 
	 * @param firstName Der Vornamen des Spielers als {@link String}.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Liefert den Nicknamen des Spielers.
	 * 
	 * @return Der Nicknamen des Spielers als {@link String}.
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * Setzt den Nicknamen des Spielers.
	 * 
	 * @param nickName Der Nicknamen des Spielers als {@link String}.
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * Liefert die E-Mail Adresse des Spielers.
	 * 
	 * @return Die E-Mail Adresse des Spielers als {@link String}.
	 */
	public String getEMail() {
		return eMail;
	}

	/**
	 * Setzt die E-Mail Adresse des Spielers.
	 * 
	 * @param eMail Die E-Mail Adresse des Spielers als {@link String}.
	 */
	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	/**
	 * Setzt das Datum des letzten Spiels des Spielers.
	 * 
	 * @param lastMatchDate Das Datum des letzten Spiels des Spielers als {@link Date}.
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

	/**
	 * Liefert die Einzelspiel-Statistik des Spielers.
	 * 
	 * @return Die Einzelspiel-Statistik des Spielers als {@link Long}.
	 */
	public Long getPlayerSingleStats() {
		return playerSingleStats;
	}

	/**
	 * Setzt die Einzelspiel-Statistik des Spielers.
	 * 
	 * @param playerSingleStats Die Einzelspiel-Statistik des Spielers als {@link Long}.
	 */
	public void setPlayerSingleStats(Long playerSingleStats) {
		this.playerSingleStats = playerSingleStats;
	}

	/**
	 * Liefert die Doppelspiel-Statistik des Spielers.
	 * 
	 * @return Die Doppelspiel-Statistik des Spielers als {@link Long}.
	 */
	public Long getPlayerDoubleStats() {
		return playerDoubleStats;
	}

	/**
	 * Setzt die Doppelspiel-Statistik des Spielers.
	 * 
	 * @param playerDoubleStats Die Doppelspiel-Statistik des Spielers als {@link Long}.
	 */
	public void setPlayerDoubleStats(Long playerDoubleStats) {
		this.playerDoubleStats = playerDoubleStats;
	}

	/**
	 * Liefert die Liste der Teams zu denen der Spieler gehört.
	 * 
	 * @return Die Liste der Teams zu denen der Spieler gehört als {@link HashSet}.
	 */
	public HashSet<Long> getTeams() {
		return teams;
	}

	/**
	 * Setzt die Liste der Teams zu denen der Spieler gehört.
	 * 
	 * @param teams Die Liste der Teams zu denen der Spieler gehört als {@link HashSet}.
	 */
	public void setTeams(HashSet<Long> teams) {
		this.teams = teams;
	}

}
