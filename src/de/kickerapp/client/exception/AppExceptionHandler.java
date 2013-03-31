package de.kickerapp.client.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;

import de.kickerapp.client.ui.dialog.AppErrorMessageBox;

/**
 * Standard-Fehlerbehandlungsroutine für Exceptions in der Applikation.
 * 
 * @author Sebastian Filke
 */
public class AppExceptionHandler implements UncaughtExceptionHandler {

	/** Der Logger der Klasse. */
	private static transient final Logger LOGGER = Logger.getLogger(AppExceptionHandler.class.getName());

	@Override
	public void onUncaughtException(Throwable caught) {
		handleException(caught);
	}

	/**
	 * Die eigentliche Fehlerbehandlung.
	 * 
	 * @param caught die Exception
	 */
	public static void handleException(final Throwable caught) {
		LOGGER.log(Level.SEVERE, caught.getMessage(), caught);

		String message = "";
		if (caught.getMessage() != null) {
			message = caught.getLocalizedMessage();
		}
		if (message.isEmpty()) {
			message = caught.getClass().getName();
		}

		final String error = getCustomStackTrace(caught);
		final AppErrorMessageBox dialog = new AppErrorMessageBox();
		if (message != null) {
			dialog.setErrorMsg(message);
		}
		// dialog.setErrorContents(error);
		dialog.show();
	}

	/**
	 * HTML Format des StackTraces.
	 * 
	 * @param caught die Exception
	 * @return StackTrace als String
	 */
	private static String getCustomStackTrace(Throwable caught) {
		final StringBuilder sb = new StringBuilder();
		sb.append(caught.toString());
		sb.append("<br/>");

		for (StackTraceElement element : caught.getStackTrace()) {
			sb.append(element);
			sb.append("<br/>");
		}
		return sb.toString();
	}

}
