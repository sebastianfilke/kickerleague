package de.kickerapp.shared.player;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Datenklasse zum Halten der Informationen für einen Spieler.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class Player implements Serializable {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -13034504212792103L;

	/** Der Primärschlüssel der Datenklasse (Datenspeicherentität). */
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private long key;

	/** Der Nachname des Spielers. */
	@Persistent
	private String lastName;

	/** Der Vorname des Spielers. */
	@Persistent
	private String firstName;

	/**
	 * Erzeugt einen neuen Spieler ohne Eingaben.
	 */
	public Player() {
		super();
	}

	/**
	 * Erzeugt einen neuen Spieler mit Vor- und Nachnamen.
	 * 
	 * @param firstName
	 *            Der Vornamen des Spielers.
	 * @param lastName
	 *            Der Nachname des Spielers als <code>String</code>.
	 */
	public Player(String lastName, String firstName) {
		this();
		this.lastName = lastName;
		this.firstName = firstName;
	}

	/**
	 * Liefert den den Primärschlüssel der Datenklasse (Datenspeicherentität).
	 * 
	 * @return Der Primärschlüssel als {@link long}.
	 */
	public long getKey() {
		return key;
	}

	/**
	 * Setzt den Nachnamen des Spielers.
	 * 
	 * @param lastName
	 *            Der Nachname des Spielers als {@link String}.
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
	 * @param firstName
	 *            Der Vornamen des Spielers als {@link String}.
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

}
