package de.kickerapp.shared.dto;

import java.util.Date;

import de.kickerapp.shared.common.BaseDto;

/**
 * Datenklasse zum Halten der Informationen eines Spielers.
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
	/** Der Nickname des Spielers. */
	private String nickName;
	/** Die E-Mail Adresse des Spielers. */
	private String eMail;
	/** Das Datum des letzten Spiels des Spielers. */
	private Date lastMatchDate;

	private PlayerSingleStatsDto playerSingleStats;

	private PlayerDoubleStatsDto playerDoubleStats;

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
		playerSingleStats = null;
		playerDoubleStats = null;
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

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	public String getEMail() {
		return eMail;
	}

	public void setLastMatchDate(Date lastMatchDate) {
		this.lastMatchDate = lastMatchDate;
	}

	public Date getLastMatchDate() {
		return lastMatchDate;
	}

	public PlayerSingleStatsDto getPlayerSingleStats() {
		return playerSingleStats;
	}

	public void setPlayerSingleStats(PlayerSingleStatsDto playerSingleStats) {
		this.playerSingleStats = playerSingleStats;
	}

	public PlayerDoubleStatsDto getPlayerDoubleStats() {
		return playerDoubleStats;
	}

	public void setPlayerDoubleStats(PlayerDoubleStatsDto playerDoubleStats) {
		this.playerDoubleStats = playerDoubleStats;
	}

}
