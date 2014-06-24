package de.kickerapp.server.dao;

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
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public class BaseDao {

	/** Der Primärschlüssel der Datenklasse (Datenspeicherentität). */
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	/**
	 * Erzeugt eine neue Basisklasse.
	 */
	public BaseDao() {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(key.getId());
		sb.append("]");

		return sb.toString();
	}

}
