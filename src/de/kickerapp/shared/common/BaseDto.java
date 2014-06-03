package de.kickerapp.shared.common;

/**
 * Die Client-Basisklasse für Datenbankobjekte.
 * 
 * @author Sebastian Filke
 */
public class BaseDto implements IBase {

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
	 * {@inheritDoc}
	 */
	@Override
	public long getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(long id) {
		this.id = id;
	}

}
