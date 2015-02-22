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
	public void onUncaughtException(Throwable caught) {
		handleException(caught, true);
	}

	/**
	 * Die eigentliche Fehlerbehandlung.
	 * 
	 * @param caught Der Fehler der aufgetreten ist.
	 * @param remoteLog <code>true</code> falls der Fehler serverseitig geloggt werden soll, andernfalls <code>false</code>.
	 */
	public void handleException(final Throwable caught, final boolean remoteLog) {
		if (remoteLog && GWT.isProdMode()) {
			final SimpleRemoteLogHandler remoteLogHandler = new SimpleRemoteLogHandler();
			final LogRecord logRecord = new LogRecord(Level.SEVERE, caught.getMessage());
			logRecord.setThrown(caught);
			remoteLogHandler.publish(logRecord);
		} else {
			LOGGER.log(Level.SEVERE, caught.getMessage(), caught);
		}

		final Throwable unwrapedCaught = unwrap(caught);

		String errorMessage = "";
		if (unwrapedCaught.getMessage() != null) {
			errorMessage = unwrapedCaught.getLocalizedMessage();
		}
		if (errorMessage.isEmpty()) {
			errorMessage = unwrapedCaught.getClass().getName();
		}

		if (errorMessage != null) {
			AppErrorDialog.getInstance().setErrorMessage(errorMessage);
			AppErrorDialog.getInstance().setDetailedErrorMessage(getErrorDetails(caught));
			AppErrorDialog.getInstance().show();
		}
	}

	/**
	 * Entpackt den Fehler zur besseren Anzeige.
	 * 
	 * @param caught Der Fehler der aufgetreten ist.
	 * @return Der entpackte Fehler.
	 */
	private Throwable unwrap(Throwable caught) {
		if (caught instanceof UmbrellaException) {
			final UmbrellaException umbrellaException = (UmbrellaException) caught;
			if (umbrellaException.getCauses().size() == 1) {
				return unwrap(umbrellaException.getCauses().iterator().next());
			}
		}
		return caught;
	}

	/**
	 * Liefert den StackTrace in einem HTML-Format.
	 * 
	 * @param caught Der Fehler der aufgetreten ist.
	 * @return Der StackTrace in einem HTML-Format.
	 */
	private String getErrorDetails(Throwable caught) {
		final StringBuilder sb = new StringBuilder();
		sb.append(caught.toString());
		sb.append("<br>");

		for (StackTraceElement element : caught.getStackTrace()) {
			sb.append(element);
			sb.append("<br>");
		}
		return sb.toString();
	}

}
