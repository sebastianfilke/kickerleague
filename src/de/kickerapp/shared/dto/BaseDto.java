package de.kickerapp.shared.dto;

import de.kickerapp.shared.common.BaseSerializable;

/**
 * Die Client-Basisklasse für Datenbankobjekte.
 * 
 * @author Sebastian Filke
 */
public class BaseDto implements BaseSerializable {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -4326262956527591125L;

	/** Die DB-ID für das Objekt. */
	private long id;

	/**
	 * Erzeugt eine neue Basisklasse.
	 */
	public BaseDto() {
		super();

		id = 0;
	}

	/**
	 * Liefert die DB-ID für das Objekt.
	 * 
	 * @return Die DB-ID für das Objekt als {@link long}.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setzt die DB-ID für das Objekt.
	 * 
	 * @param id Die DB-ID für das Objekt als {@link long}.
	 */
	public void setId(long id) {
		this.id = id;
	}

}
