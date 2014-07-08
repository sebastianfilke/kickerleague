package de.kickerapp.client.exception;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.logging.client.SimpleRemoteLogHandler;

import de.kickerapp.client.ui.dialog.AppErrorDialog;

/**
 * Standard-Fehlerbehandlungsroutine für Fehler in der Applikation.
 * 
 * @author Sebastian Filke
 */
public final class AppExceptionHandler implements UncaughtExceptionHandler {

	/** Der Logger der Klasse. */
	private static transient final Logger LOGGER = Logger.getLogger(AppExceptionHandler.class.getName());

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
	 * Erzeugt eine neue Standard-Fehlerbehandlungsroutine.
	 */
	private AppExceptionHandler() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUncaughtException(Throwable caught) {
		handleException(caught);
	}

	/**
	 * Die eigentliche Fehlerbehandlung.
	 * 
	 * @param caught Der Fehler der aufgetreten ist.
	 */
	public void handleException(final Throwable caught) {
		// LOGGER.log(Level.SEVERE, caught.getMessage(), caught);

		final SimpleRemoteLogHandler remoteLog = new SimpleRemoteLogHandler();
		final LogRecord logRecord = new LogRecord(Level.SEVERE, caught.getMessage());
		logRecord.setThrown(caught);
		remoteLog.publish(logRecord);

		String errorMessage = "";
		if (caught.getMessage() != null) {
			errorMessage = caught.getLocalizedMessage();
		}
		if (errorMessage.isEmpty()) {
			errorMessage = caught.getClass().getName();
		}

		// final String errorDetails = getCustomStackTrace(caught);
		if (errorMessage != null) {
			AppErrorDialog.getInstance().setErrorMessage(errorMessage);
		}
		// dialog.setErrorContents(errorDetails);
		AppErrorDialog.getInstance().show();
	}

	/**
	 * Liefert den StackTrace in einem HTML-Format.
	 * 
	 * @param caught Der Fehler der aufgetreten ist.
	 * @return Der StackTrace in einem HTML-Format.
	 */
	private String getCustomStackTrace(Throwable caught) {
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
