package de.kickerapp.server.entity;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Die Basisdatenklasse zum Halten der Informationen für Objektklassen.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
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
	 * Liefert den Primärschlüssel der Datenklasse (Datenspeicherentität).
	 * 
	 * @return Der Primärschlüssel als {@link Key}.
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * Setzt den Primärschlüssel der Datenklasse (Datenspeicherentität).
	 * 
	 * @param key Der Primärschlüssel als {@link Key}.
	 */
	public void setKey(Key key) {
		this.key = key;
	}

}
