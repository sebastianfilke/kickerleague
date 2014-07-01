package de.kickerapp.shared.common;

/**
 * Die Client-Basisklasse für Datenbankobjekte.
 * 
 * @author Sebastian Filke
 */
public class BaseDto implements BaseSerializable {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -4326262956527591125L;

	/** Die DB-Id für das Objekt. */
	private long id;

	/**
	 * Erzeugt eine neue Basisklasse.
	 */
	public BaseDto() {
		this.id = 0;
	}

	/**
	 * Liefert die DB-Id für das Objekt.
	 * 
	 * @return Die DB-Id für das Objekt als {@link long}.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setzt die DB-Id für das Objekt.
	 * 
	 * @param id Die DB-Id für das Objekt als {@link long}.
	 */
	public void setId(long id) {
		this.id = id;
	}

}
