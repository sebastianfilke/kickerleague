package de.kickerapp.server.dto;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Die Basisdatenklasse zum Halten der Informationen für Objektklassen.
 * 
 * @author Sebastian Filke
 */
public class BaseEntity implements Serializable {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -7621677056319765775L;

	/** Der Primärschlüssel der Datenklasse (Datenspeicherentität). */
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	/**
	 * Erzeugt eine neue Basisklasse.
	 */
	public BaseEntity() {
		super();
	}

	/**
	 * Liefert den den Primärschlüssel der Datenklasse (Datenspeicherentität).
	 * 
	 * @return Der Primärschlüssel als {@link Key}.
	 */
	public Key getKey() {
		return key;
	}

}
