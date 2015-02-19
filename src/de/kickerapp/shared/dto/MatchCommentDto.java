package de.kickerapp.shared.dto;

/**
 * Client-Datenklasse zum Halten der Kommentare für ein Spiel.
 * 
 * @author Sebastian Filke
 */
public class MatchCommentDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Der Kommentar. */
	private String comment;

	/**
	 * Erzeugt einen neuen Kommentar ohne Angaben.
	 */
	public MatchCommentDto() {
		super();

		comment = "";
	}

	/**
	 * Erzeugt einen neuen Kommentar mit Text und Spiel.
	 * 
	 * @param comment Der Kommentar als {@link String}.
	 */
	public MatchCommentDto(String comment) {
		this();
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
		sb.append("id=").append(getId()).append(", ");
		sb.append("comment=").append(comment);
		sb.append("]");

		return sb.toString();
	}

}
