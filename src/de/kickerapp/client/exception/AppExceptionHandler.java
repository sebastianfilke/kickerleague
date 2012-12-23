package de.kickerapp.client.exception;

import java.util.logging.Logger;

import de.kickerapp.client.ui.dialog.AppErrorMessageBox;

/**
 * Standard Fehlerbehandlungsroutine für Exceptions in der Applikation.
 * 
 * @author Sebastian Filke
 */
public class AppExceptionHandler {

	/** Der Logger der Klasse. */
	private static transient final Logger LOGGER = Logger.getLogger(AppExceptionHandler.class.getName());

	/**
	 * Privater Konstruktor soll nicht von außen aufgerufen werden.
	 */
	private AppExceptionHandler() {
	}

	/**
	 * Die eigentliche Fehlerbehandlung.
	 * 
	 * @param throwable die Exception
	 */
	public static void handleException(final Throwable throwable) {
		LOGGER.fine("AppExceptionHandler.handleException: " + throwable);

		processException(throwable);
	}

	/**
	 * Startet die Fehlerbehandlung.
	 * 
	 * @param theCaught Exception
	 */
	private static void processException(Throwable theCaught) {
		String msg = "";

		if (theCaught != null && theCaught.getMessage() != null) {
			msg = theCaught.getLocalizedMessage();
		}

		if ("".equals(msg)) {
			msg = theCaught.getClass().getName();
		}

		final String error = getCustomStackTrace(theCaught);
		final AppErrorMessageBox dialog = new AppErrorMessageBox();

		if (msg != null) {
			// dialog.setErrorMsg(msg);
		}

		LOGGER.severe(error.replaceAll("<br>", "\n"));

		// dialog.setErrorContents(error);
		dialog.show();
	}

	/**
	 * Fehlertext formatieren
	 * 
	 * @param theMessage Fehlertext
	 * @return Formatierter Fehlertext
	 */
	private static String extractMessage(String theMessage) {
		if (theMessage == null || theMessage.equals("")) {
			return "";
		}

		final int start = theMessage.indexOf("ErrorCodeRuntimeException[message=");

		if (start == -1) {
			return theMessage;
		}

		return theMessage.substring(theMessage.indexOf("---", start), theMessage.indexOf("]", start)).replaceAll("\n", "<br>");
	}

	/**
	 * HTML Format des StackTraces.
	 * 
	 * @param theThrowable die Exception
	 * @return StackTrace als String
	 */
	private static String getCustomStackTrace(Throwable theThrowable) {
		if (theThrowable == null) {
			return "";
		}

		final StringBuilder result = new StringBuilder();
		result.append(theThrowable.toString());
		result.append("<br>");

		for (StackTraceElement element : theThrowable.getStackTrace()) {
			result.append(element);
			result.append("<br>");
		}
		return result.toString();
	}

}
