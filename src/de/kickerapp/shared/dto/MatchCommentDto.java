package de.kickerapp.shared.dto;

import java.util.Date;

import de.kickerapp.shared.common.BaseDto;

/**
 * Client-Datenklasse zum Halten der Kommentare für ein Spiel.
 * 
 * @author Sebastian Filke
 */
public class MatchCommentDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1965905725630960510L;

	/** Der Kommentar. */
	private String comment;
	/** Das Kommentardatum. */
	private Date commentDate;
	/** Der Spieler. */
	private PlayerDto playerDto;

	/**
	 * Erzeugt ein neues Spiel ohne Angaben.
	 */
	public MatchCommentDto() {
		super();

		comment = "";
		commentDate = null;
		playerDto = null;
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
	 * Liefert den Spieler.
	 * 
	 * @return Der Spieler als {@link PlayerDto}.
	 */
	public PlayerDto getPlayerDto() {
		return playerDto;
	}

	/**
	 * Setzt den Spieler.
	 * 
	 * @param playerDto Der Spieler als {@link PlayerDto}.
	 */
	public void setPlayerDto(PlayerDto playerDto) {
		this.playerDto = playerDto;
	}

}
