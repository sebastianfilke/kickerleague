package de.kickerapp.shared.match;

import java.util.Date;

import de.kickerapp.shared.common.BaseData;

/**
 * @author Basti
 */
public class PlayerDto extends BaseData implements IPlayer {

	/**
	 * 
	 */
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

	/**
	 * Erzeugt einen neuen Spieler ohne Angaben.
	 */
	public PlayerDto() {
		super();
	}

	/**
	 * Erzeugt einen neuen Spieler mit Vor- und Nachnamen.
	 * 
	 * @param firstName Der Vornamen des Spielers.
	 * @param lastName Der Nachname des Spielers als <code>String</code>.
	 */
	public PlayerDto(String lastName, String firstName) {
		this();
		this.lastName = lastName;
		this.firstName = firstName;
		setLabel(firstName + " " + lastName);
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

}
