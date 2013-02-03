package de.kickerapp.shared.common;

/**
 * Die Basisklasse für Datenbankobjekte im Client.
 * 
 * @author Sebastian Filke
 */
public class BaseData implements IBase {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -4326262956527591125L;

	/** Die DB-ID für das Objekt. */
	private long id;
	/** Der Anzeigetext für ein ausgewähltes Objekt. */
	private String label;

	/**
	 * Erzeugt eine neue Basisklasse.
	 */
	public BaseData() {
		this.id = 0;
		this.label = "";
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return label;
	}

}
