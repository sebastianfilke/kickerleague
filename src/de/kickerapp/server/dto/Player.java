package de.kickerapp.server.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import de.kickerapp.shared.common.Tendency;

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
	/** Die Teams zu denen der Spieler gehört. */
	@Persistent
	private Set<Long> teams;
	/** Die Statistik des Spielers. */
	@Embedded
	@Persistent(defaultFetchGroup = "true")
	private PlayerStats stats;

	/**
	 * Erzeugt einen neuen Spieler ohne Angaben und einer leeren Statistik.
	 */
	public Player() {
		super();
		stats = new PlayerStats();
	}

	/**
	 * Erzeugt einen neuen Spieler mit Vor- und Nachnamen.
	 * 
	 * @param firstName Der Vornamen des Spielers als <code>String</code>.
	 * @param lastName Der Nachname des Spielers als <code>String</code>.
	 */
	public Player(String lastName, String firstName) {
		this();
		this.lastName = lastName;
		this.firstName = firstName;
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
	 * Liefert den Nachnamen des Spielers.
	 * 
	 * @return Der Nachname des Spielers.
	 */
	public String getLastName() {
		return lastName;
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
	 * Liefert den Vornamen des Spielers.
	 * 
	 * @return Der Vornamen des Spielers.
	 */
	public String getFirstName() {
		return firstName;
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
	 * Liefert den Nicknamen des Spielers.
	 * 
	 * @return Der Nicknamen des Spielers.
	 */
	public String getNickName() {
		return nickName;
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
	 * Liefert die E-Mail Adresse des Spielers.
	 * 
	 * @return Die E-Mail Adresse des Spielers als {@link String}.
	 */
	public String getEMail() {
		return eMail;
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

	public PlayerStats getPlayerStats() {
		return stats;
	}

	public void setPlayerStats(PlayerStats stats) {
		this.stats = stats;
	}

	@PersistenceCapable
	public static class PlayerStats implements Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = 1947718587252967650L;

		@Persistent
		private Integer singleWins;

		@Persistent
		private Integer singleLosses;

		@Persistent
		private Integer singleShotGoals;

		@Persistent
		private Integer singleGetGoals;

		@Persistent
		private Integer doubleWins;

		@Persistent
		private Integer doubleLosses;

		@Persistent
		private Integer doubleShotGoals;

		@Persistent
		private Integer doubleGetGoals;

		@Persistent
		private Integer lastTablePlace;

		@Persistent
		private Tendency tendency;

		@Persistent
		private Integer points;

		public PlayerStats() {
			singleWins = 0;
			singleLosses = 0;
			singleShotGoals = 0;
			singleGetGoals = 0;
			doubleWins = 0;
			doubleWins = 0;
			doubleLosses = 0;
			doubleShotGoals = 0;
			doubleGetGoals = 0;
			lastTablePlace = 0;
			tendency = Tendency.Constant;
			points = 1000;
		}

		public Integer getSingleWins() {
			return singleWins;
		}

		public void setSingleWins(Integer singleWins) {
			this.singleWins = singleWins;
		}

		public Integer getSingleLosses() {
			return singleLosses;
		}

		public void setSingleLosses(Integer singleLosses) {
			this.singleLosses = singleLosses;
		}

		public Integer getSingleShotGoals() {
			return singleShotGoals;
		}

		public void setSingleShotGoals(Integer singleShotGoals) {
			this.singleShotGoals = singleShotGoals;
		}

		public Integer getSingleGetGoals() {
			return singleGetGoals;
		}

		public void setSingleGetGoals(Integer singleGetGoals) {
			this.singleGetGoals = singleGetGoals;
		}

		public Integer getDoubleWins() {
			return doubleWins;
		}

		public void setDoubleWins(Integer doubleWins) {
			this.doubleWins = doubleWins;
		}

		public Integer getDoubleLosses() {
			return doubleLosses;
		}

		public void setDoubleLosses(Integer doubleLosses) {
			this.doubleLosses = doubleLosses;
		}

		public Integer getDoubleShotGoals() {
			return doubleShotGoals;
		}

		public void setDoubleShotGoals(Integer doubleShotGoals) {
			this.doubleShotGoals = doubleShotGoals;
		}

		public Integer getDoubleGetGoals() {
			return doubleGetGoals;
		}

		public void setDoubleGetGoals(Integer doubleGetGoals) {
			this.doubleGetGoals = doubleGetGoals;
		}

		public Integer getLastTablePlace() {
			return lastTablePlace;
		}

		public void setLastTablePlace(Integer lastTablePlace) {
			this.lastTablePlace = lastTablePlace;
		}

		public Tendency getTendency() {
			return tendency;
		}

		public void setTendency(Tendency tendency) {
			this.tendency = tendency;
		}

		public Integer getPoints() {
			return points;
		}

		public void setPoints(Integer points) {
			this.points = points;
		}
	}

}
