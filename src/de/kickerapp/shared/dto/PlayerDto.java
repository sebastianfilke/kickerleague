package de.kickerapp.shared.dto;

import java.util.Date;

import de.kickerapp.shared.common.BaseData;

/**
 * Datenklasse zum Halten der Informationen eines Spielers.
 * 
 * @author Sebastian Filke
 */
public class PlayerDto extends BaseData implements IPlayer {

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
		setLabel(firstName + " " + lastName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLastName() {
		return lastName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFirstName() {
		return firstName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNickName() {
		return nickName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getEMail() {
		return eMail;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLastMatchDate(Date lastMatchDate) {
		this.lastMatchDate = lastMatchDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getLastMatchDate() {
		return lastMatchDate;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerSingleStatsDto getPlayerSingleStats() {
		return playerSingleStats;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlayerSingleStats(PlayerSingleStatsDto playerSingleStats) {
		this.playerSingleStats = playerSingleStats;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerDoubleStatsDto getPlayerDoubleStats() {
		return playerDoubleStats;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlayerDoubleStats(PlayerDoubleStatsDto playerDoubleStats) {
		this.playerDoubleStats = playerDoubleStats;
	}

}
