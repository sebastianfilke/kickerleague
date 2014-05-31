package de.kickerapp.server.entity;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Text;

/**
 * Datenklasse zum Halten der Kommentare für ein Spiel.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class MatchComment extends BaseEntity {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -6288965122886332873L;

	/** Der Kommentar. */
	@Persistent
	private Text comment;
	/** Das Spiel des Kommentars. */
	@Persistent
	private Long match;

	/**
	 * Erzeugt einen neuen leeren Kommentar.
	 */
	public MatchComment() {
		super();

		comment = null;
		match = null;
	}

	/**
	 * Erzeugt einen neuen Kommentar mit Text und Spiel.
	 * 
	 * @param comment Der Kommentar als {@link Text}.
	 * @param match Das Spiel des Kommentars {@link Long}.
	 */
	public MatchComment(Text comment, Long match) {
		this.comment = comment;
		this.match = match;
	}

	/**
	 * Liefert den Kommentar.
	 * 
	 * @return Der Kommentar {@link String}.
	 */
	public Text getComment() {
		return comment;
	}

	/**
	 * Setzt den Kommentar.
	 * 
	 * @param comment Der Kommentar {@link String}.
	 */
	public void setComment(Text comment) {
		this.comment = comment;
	}

	/**
	 * Liefert das Spiel des Kommentars.
	 * 
	 * @return Das Spiel des Kommentars als {@link Long}.
	 */
	public Long getMatch() {
		return match;
	}

	/**
	 * Setzt das Spiel des Kommentars.
	 * 
	 * @param match Das Spiel des Kommentars als {@link Long}.
	 */
	public void setMatch(Long match) {
		this.match = match;
	}

}
