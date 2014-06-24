package de.kickerapp.server.dao;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Datenklasse zum Halten der Kommentare für ein Spiel.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
public class MatchComment extends BaseDao {

	/** Der Kommentar. */
	@Persistent
	private String comment;

	/**
	 * Erzeugt einen neuen leeren Kommentar.
	 */
	public MatchComment() {
		super();

		comment = null;
	}

	/**
	 * Erzeugt einen neuen Kommentar mit Text und Spiel.
	 * 
	 * @param comment Der Kommentar als {@link String}.
	 */
	public MatchComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Liefert den Kommentar.
	 * 
	 * @return Der Kommentar {@link String}.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Setzt den Kommentar.
	 * 
	 * @param comment Der Kommentar {@link String}.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(getKey().getId()).append(", ");
		sb.append("comment=").append(comment);
		sb.append("]");

		return sb.toString();
	}

}
