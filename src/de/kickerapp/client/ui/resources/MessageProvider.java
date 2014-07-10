package de.kickerapp.client.ui.resources;

import com.google.gwt.core.client.GWT;

import de.kickerapp.client.ui.resources.messages.KickerMessages;

/**
 * Klasse für den komfortableren Zugriff auf die sprachabhängigen Texte.
 * 
 * @author Sebastian Filke
 */
public final class MessageProvider {

	/** Die Messages der Applikation. */
	private final KickerMessages messages;

	/**
	 * Klasse zum Halten der Instanz von {@link MessageProvider}.
	 * 
	 * @author Sebastian Filke
	 */
	private static class LazyHolder {

		/** Die Instanz der Klasse {@link MessageProvider}. */
		private static final MessageProvider INSTANCE = new MessageProvider();
	}

	/**
	 * Privater Konstruktor soll nicht von außen aufgerufen werden.
	 */
	private MessageProvider() {
		messages = GWT.create(KickerMessages.class);
	}

	/**
	 * Statische Methode <code>getInstance()</code> liefert die einzige Instanz der {@link MessageProvider}-Klasse.
	 * 
	 * @return Die einzige Instanz der {@link MessageProvider}-Klasse.
	 */
	private static MessageProvider getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * Liefert die Messages der Applikation.
	 * 
	 * @return Die Messages der Applikation.
	 */
	public static KickerMessages get() {
		return getInstance().messages;
	}

}
