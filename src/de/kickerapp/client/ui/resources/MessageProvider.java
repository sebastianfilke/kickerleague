package de.kickerapp.client.ui.resources;

import com.google.gwt.core.client.GWT;

public class MessageProvider {

	/**
	 * @author Basti
	 */
	private static class Holder {
		private static final KickerMessages INSTANCE = GWT.create(KickerMessages.class);
	}

	/**
	 * Konstruktor soll nicht von außen aufgerufen werden.
	 */
	private MessageProvider() {
	}

	/**
	 * Statische Methode get() liefert die Instanz der Messages.
	 * 
	 * @return Das Interface der Texte.
	 */
	public static KickerMessages get() {
		return Holder.INSTANCE;
	}

}
