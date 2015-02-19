package de.kickerapp.client.exception;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.logging.client.SimpleRemoteLogHandler;

import de.kickerapp.client.ui.dialog.AppErrorDialog;

/**
 * Standard-Fehlerbehandlungsroutine für Fehler in der Applikation.
 * 
 * @author Sebastian Filke
 */
public final class AppExceptionHandler implements UncaughtExceptionHandler {

	/** Der Logger der Klasse. */
	private static final transient Logger LOGGER = Logger.getLogger(AppExceptionHandler.class.getName());

	/**
	 * Erzeugt eine neue Standard-Fehlerbehandlungsroutine.
	 */
	private AppExceptionHandler() {
		super();
	}

	/**
	 * Klasse zum Halten der Instanz von {@link AppExceptionHandler}.
	 * 
	 * @author Sebastian Filke
	 */
	private static class LazyHolder {

		/** Die Instanz der Klasse {@link AppExceptionHandler}. */
		private static final AppExceptionHandler INSTANCE = new AppExceptionHandler();
	}

	/**
	 * Liefert die einzige Instanz der Klasse {@link AppExceptionHandler}.
	 * 
	 * @return Die einzige Instanz der Klasse {@link AppExceptionHandler}.
	 */
	public static AppExceptionHandler getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUncaughtException(Throwable cause) {
		handleException(cause, true);
	}

	/**
	 * Die eigentliche Fehlerbehandlung.
	 * 
	 * @param cause Der Fehler der aufgetreten ist.
	 * @param remoteLog <code>true</code> falls der Fehler serverseitig geloggt werden soll, andernfalls <code>false</code>.
	 */
	public void handleException(final Throwable cause, final boolean remoteLog) {
		if (remoteLog && GWT.isProdMode()) {
			final SimpleRemoteLogHandler remoteLogHandler = new SimpleRemoteLogHandler();
			final LogRecord logRecord = new LogRecord(Level.SEVERE, cause.getMessage());
			logRecord.setThrown(cause);
			remoteLogHandler.publish(logRecord);
		} else {
			LOGGER.log(Level.SEVERE, cause.getMessage(), cause);
		}

		final Throwable unwrapedCause = unwrap(cause);

		String errorMessage = "";
		if (unwrapedCause.getMessage() != null) {
			errorMessage = unwrapedCause.getLocalizedMessage();
		}
		if (errorMessage.isEmpty()) {
			errorMessage = unwrapedCause.getClass().getName();
		}

		// final String errorDetails = getCustomStackTrace(caught);
		if (errorMessage != null) {
			AppErrorDialog.getInstance().setErrorMessage(errorMessage);
			AppErrorDialog.getInstance().show();
		}
		// dialog.setErrorContents(errorDetails);
	}

	/**
	 * Entpackt den Fehler zur besseren Anzeige.
	 * 
	 * @param cause Der Fehler der aufgetreten ist.
	 * @return Der entpackte Fehler.
	 */
	private Throwable unwrap(Throwable cause) {
		if (cause instanceof UmbrellaException) {
			final UmbrellaException umbrellaException = (UmbrellaException) cause;
			if (umbrellaException.getCauses().size() == 1) {
				return unwrap(umbrellaException.getCauses().iterator().next());
			}
		}
		return cause;
	}

	/**
	 * Liefert den StackTrace in einem HTML-Format.
	 * 
	 * @param cause Der Fehler der aufgetreten ist.
	 * @return Der StackTrace in einem HTML-Format.
	 */
	private String getCustomStackTrace(Throwable cause) {
		final StringBuilder sb = new StringBuilder();
		sb.append(cause.toString());
		sb.append("<br>");

		for (StackTraceElement element : cause.getStackTrace()) {
			sb.append(element);
			sb.append("<br>");
		}
		return sb.toString();
	}

}
