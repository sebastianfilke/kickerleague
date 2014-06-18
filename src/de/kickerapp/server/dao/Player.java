package de.kickerapp.server.dao;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchGroups;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.datanucleus.annotations.Unowned;

import de.kickerapp.server.dao.fetchplans.PlayerPlan;

/**
 * Datenklasse zum Halten der Informationen für einen Spieler.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
@FetchGroups({ @FetchGroup(name = PlayerPlan.ALLSTATS, members = { @Persistent(name = "playerSingleStats"), @Persistent(name = "playerDoubleStats") }),
		@FetchGroup(name = PlayerPlan.PLAYERSINGLESTATS, members = { @Persistent(name = "playerSingleStats") }),
		@FetchGroup(name = PlayerPlan.PLAYERDOUBLESTATS, members = { @Persistent(name = "playerDoubleStats") }),
		@FetchGroup(name = PlayerPlan.TEAMS, members = { @Persistent(name = "teams") }) })
public class Player extends BaseDao {

	/** Der Nachname des Spielers. */
	@Persistent
	private String lastName;
	/** Der Vorname des Spielers. */
	@Persistent
	private String firstName;
	/** Der Spitzname des Spielers. */
	@Persistent
	private String nickName;
	/** Die E-Mail Adresse des Spielers. */
	@Persistent
	private String eMail;
	/** Das Datum des letzten Spiels des Spielers. */
	@Persistent
	private Date lastMatchDate;
	/** Die Einzelspiel-Statistik des Spielers. */
	@Element(dependent = "true")
	@Persistent(defaultFetchGroup = "false")
	private PlayerSingleStats playerSingleStats;
	/** Die Doppelspiel-Statistik des Spielers. */
	@Element(dependent = "true")
	@Persistent(defaultFetchGroup = "false")
	private PlayerDoubleStats playerDoubleStats;
	/** Die Liste der Teams zu denen der Spieler gehört. */
	@Unowned
	@Persistent(defaultFetchGroup = "false")
	private ArrayList<Team> teams;

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
		playerSingleStats = null;
		playerDoubleStats = null;
		teams = new ArrayList<Team>();
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
	 * Liefert den Spitznamen des Spielers.
	 * 
	 * @return Der Spitzname des Spielers als {@link String}.
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * Setzt den Spitznamen des Spielers.
	 * 
	 * @param nickName Der Spitzname des Spielers als {@link String}.
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
	 * @return Die Einzelspiel-Statistik des Spielers als {@link PlayerSingleStats}.
	 */
	public PlayerSingleStats getPlayerSingleStats() {
		return playerSingleStats;
	}

	/**
	 * Setzt die Einzelspiel-Statistik des Spielers.
	 * 
	 * @param playerSingleStats Die Einzelspiel-Statistik des Spielers als {@link PlayerSingleStats}.
	 */
	public void setPlayerSingleStats(PlayerSingleStats playerSingleStats) {
		this.playerSingleStats = playerSingleStats;
	}

	/**
	 * Liefert die Doppelspiel-Statistik des Spielers.
	 * 
	 * @return Die Doppelspiel-Statistik des Spielers als {@link PlayerDoubleStats}.
	 */
	public PlayerDoubleStats getPlayerDoubleStats() {
		return playerDoubleStats;
	}

	/**
	 * Setzt die Doppelspiel-Statistik des Spielers.
	 * 
	 * @param playerDoubleStats Die Doppelspiel-Statistik des Spielers als {@link PlayerDoubleStats}.
	 */
	public void setPlayerDoubleStats(PlayerDoubleStats playerDoubleStats) {
		this.playerDoubleStats = playerDoubleStats;
	}

	/**
	 * Liefert die Liste der Teams zu denen der Spieler gehört.
	 * 
	 * @return Die Liste der Teams zu denen der Spieler gehört als {@link ArrayList}.
	 */
	public ArrayList<Team> getTeams() {
		return teams;
	}

	/**
	 * Setzt die Liste der Teams zu denen der Spieler gehört.
	 * 
	 * @param teams Die Liste der Teams zu denen der Spieler gehört als {@link ArrayList}.
	 */
	public void setTeams(ArrayList<Team> teams) {
		this.teams = teams;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getName());
		sb.append(" [");
		sb.append("id=").append(getKey().getId()).append(", ");
		sb.append("lastName=").append(lastName).append(", ");
		sb.append("firstName=").append(firstName).append(", ");
		sb.append("nickName=").append(nickName).append(", ");
		sb.append("]");

		return sb.toString();
	}

}
