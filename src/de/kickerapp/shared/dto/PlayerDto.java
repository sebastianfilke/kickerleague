package de.kickerapp.shared.dto;

import java.util.Date;

import de.kickerapp.shared.common.BaseDto;

/**
 * Client-Datenklasse zum Halten der Informationen eines Spielers.
 * 
 * @author Sebastian Filke
 */
public class PlayerDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -8362977112070597348L;

	/** Der Nachname des Spielers. */
	private String lastName;
	/** Der Vorname des Spielers. */
	private String firstName;
	/** Der Spitzname des Spielers. */
	private String nickName;
	/** Die E-Mail Adresse des Spielers. */
	private String eMail;
	/** Das Datum des letzten Spiels des Spielers. */
	private Date lastMatchDate;
	/** Die Einzelspiel-Statistik des Spielers. */
	private PlayerSingleStatsDto playerSingleStatsDto;
	/** Die Doppelspiel-Statistik des Spielers. */
	private PlayerDoubleStatsDto playerDoubleStatsDto;

	/**
	 * Erzeugt einen neuen Spieler ohne Angaben.
	 */
	public PlayerDto() {
		super();

		lastName = "";
		firstName = "";
		nickName = "";
		eMail = "";
		lastMatchDate = null;
		playerSingleStatsDto = null;
		playerDoubleStatsDto = null;
	}

	/**
	 * Erzeugt einen neuen Spieler mit Vor- und Nachnamen.
	 * 
	 * @param lastName Der Nachname des Spielers als <code>String</code>.
	 * @param firstName Der Vornamen des Spielers als <code>String</code>.
	 */
	public PlayerDto(String lastName, String firstName) {
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
	 * @return Die Einzelspiel-Statistik des Spielers als {@link PlayerSingleStatsDto}.
	 */
	public PlayerSingleStatsDto getPlayerSingleStatsDto() {
		return playerSingleStatsDto;
	}

	/**
	 * Setzt die Einzelspiel-Statistik des Spielers.
	 * 
	 * @param playerSingleStatsDto Die Einzelspiel-Statistik des Spielers als {@link PlayerSingleStatsDto}.
	 */
	public void setPlayerSingleStatsDto(PlayerSingleStatsDto playerSingleStatsDto) {
		this.playerSingleStatsDto = playerSingleStatsDto;
	}

	/**
	 * Liefert die Doppelspiel-Statistik des Spielers.
	 * 
	 * @return Die Doppelspiel-Statistik des Spielers als {@link PlayerDoubleStatsDto}.
	 */
	public PlayerDoubleStatsDto getPlayerDoubleStatsDto() {
		return playerDoubleStatsDto;
	}

	/**
	 * Setzt die Doppelspiel-Statistik des Spielers.
	 * 
	 * @param playerDoubleStatsDto Die Doppelspiel-Statistik des Spielers als {@link PlayerDoubleStatsDto}.
	 */
	public void setPlayerDoubleStatsDto(PlayerDoubleStatsDto playerDoubleStatsDto) {
		this.playerDoubleStatsDto = playerDoubleStatsDto;
	}

}
