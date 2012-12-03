package de.kickerapp.shared.common;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

public class BaseEntity implements Serializable {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -7621677056319765775L;

	public BaseEntity() {
		super();
	}

	/** Der Primärschlüssel der Datenklasse (Datenspeicherentität). */
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private long key;

	/**
	 * Liefert den den Primärschlüssel der Datenklasse (Datenspeicherentität).
	 * 
	 * @return Der Primärschlüssel als {@link long}.
	 */
	public long getKey() {
		return key;
	}

}
