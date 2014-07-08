package de.kickerapp.server.dao;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Datenklasse zum Halten der Sequenz-IDs für eine Entität.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
public class Sequence extends BaseDao {

	/** Der Name der Sequenz. */
	@Persistent
	private String sequenceName;
	/** Die aktuelle ID für die Sequenz. */
	@Persistent
	private Integer sequenceID;

	/**
	 * Erzeugt eine neue Sequenz.
	 */
	public Sequence() {
		super();

		sequenceName = "";
		sequenceID = 0;
	}

	/**
	 * Liefert den Namen der Sequenz.
	 * 
	 * @return Der Name der Sequenz als {@link String}.
	 */
	public String getSequenceName() {
		return sequenceName;
	}

	/**
	 * Setzt den Namen der Sequenz.
	 * 
	 * @param sequenceName Der Name der Sequenz als {@link String}.
	 */
	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	/**
	 * Liefert die aktuelle ID für die Sequenz.
	 * 
	 * @return Der aktuelle ID für die Sequenz als {@link Integer}.
	 */
	public Integer getSequenceID() {
		return sequenceID;
	}

	/**
	 * Setzt die aktuelle ID für die Sequenz.
	 * 
	 * @param sequenceID Die aktuelle ID für die Sequenz als {@link Integer}.
	 */
	public void setSequenceID(Integer sequenceID) {
		this.sequenceID = sequenceID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(getKey().getId()).append(", ");
		sb.append("sequenceName=").append(sequenceName).append(", ");
		sb.append("sequenceID=").append(sequenceID);
		sb.append("]");

		return sb.toString();
	}

}
