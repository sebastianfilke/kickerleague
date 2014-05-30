package de.kickerapp.server.entity;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

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
	private String comment;
	/** Das Kommentardatum. */
	@Persistent
	private Date commentDate;
	/** Die Datenbank-Id des Spielers. */
	@Persistent
	private Long player;
	/** Die Datenbank-Id des Spiels. */
	@Persistent
	private Long match;

	/**
	 * Erzeugt einen neuen leeren Kommentar.
	 */
	public MatchComment() {
		super();

		comment = "";
		commentDate = null;
		player = null;
		match = null;
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
	 * Liefert das Kommentardatum.
	 * 
	 * @return Das Kommentardatum {@link Date}.
	 */
	public Date getCommentDate() {
		return commentDate;
	}

	/**
	 * Liefert das Kommentardatum.
	 * 
	 * @param commentDate Das Kommentardatum {@link Date}.
	 */
	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	/**
	 * Liefert die Datenbank-Id des Spielers.
	 * 
	 * @return Die Datenbank-Id des Spielers als {@link Long}.
	 */
	public Long getPlayer() {
		return player;
	}

	/**
	 * Setzt die Datenbank-Id des Spielers.
	 * 
	 * @param player Die Datenbank-Id des Spielers als {@link Long}.
	 */
	public void setPlayer(Long player) {
		this.player = player;
	}

	/**
	 * Liefert die Datenbank-Id des Spiels.
	 * 
	 * @return Die Datenbank-Id des Spiels als {@link Long}.
	 */
	public Long getMatch() {
		return match;
	}

	/**
	 * Setzt die Datenbank-Id des Spiels.
	 * 
	 * @param match Die Datenbank-Id des Spiels als {@link Long}.
	 */
	public void setMatch(Long match) {
		this.match = match;
	}

}
