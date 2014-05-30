package de.kickerapp.shared.dto;

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
	/** Das Spiel des Kommentars. */
	private MatchDto matchDto;

	/**
	 * Erzeugt einen neuen Kommentar ohne Angaben.
	 */
	public MatchCommentDto() {
		super();

		comment = "";
		matchDto = null;
	}

	/**
	 * Erzeugt einen neuen Kommentar mit Text und Spiel.
	 * 
	 * @param comment Der Kommentar als {@link String}.
	 * @param matchDto Das Spiel des Kommentars {@link MatchDto}.
	 */
	public MatchCommentDto(String comment, MatchDto matchDto) {
		this();
		this.comment = comment;
		this.matchDto = matchDto;
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
	 * Liefert das Spiel des Kommentars.
	 * 
	 * @return Das Spiel des Kommentars als {@link MatchDto}.
	 */
	public MatchDto getMatchDto() {
		return matchDto;
	}

	/**
	 * Setzt das Spiel des Kommentars.
	 * 
	 * @param matchDto Das Spiel des Kommentars als {@link MatchDto}.
	 */
	public void setMatchDto(MatchDto matchDto) {
		this.matchDto = matchDto;
	}

}
