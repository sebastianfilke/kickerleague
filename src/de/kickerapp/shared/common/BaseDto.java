package de.kickerapp.shared.common;

/**
 * Die Basisklasse für Datenbankobjekte im Client.
 * 
 * @author Sebastian Filke
 */
public class BaseDto implements IBase {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -4326262956527591125L;

	/** Die DB-ID für das Objekt. */
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
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getId() {
		return id;
	}

}
