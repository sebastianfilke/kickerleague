package de.kickerapp.shared.exception;

/**
 * Basis-Kasse für alle Fehler in der Applikation.
 * 
 * @author Sebastian Filke
 */
public class KickerLeagueException extends Exception {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Erstellt einen neuen Fehler.
	 */
	public KickerLeagueException() {
		super();
	}

	/**
	 * Erstellt einen neuen Fehler mit übergebener Fehlermeldung.
	 * 
	 * @param message Die Fehlermeldung als {@link String}.
	 */
	public KickerLeagueException(String message) {
		super(message);
	}

	/**
	 * Erstellt einen neuen Fehler mit übergebener Fehlermeldung und aufgetretenem Fehler.
	 * 
	 * @param message Die Fehlermeldung als {@link String}.
	 * @param cause Der Fehler der aufgetreten ist.
	 */
	public KickerLeagueException(String message, Throwable cause) {
		super(message, cause);
	}

}
