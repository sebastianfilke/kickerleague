package de.kickerapp.client.services;

import com.google.gwt.core.client.GWT;

public class ServiceProvider {
	/**
	 * @author Basti
	 */
	private static class Holder {
		private static final MatchServiceAsync matchService = GWT.create(MatchService.class);
	}

	/**
	 * Konstruktor soll nicht von außen aufgerufen werden.
	 */
	private ServiceProvider() {
	}

	/**
	 * Statische Methode get() liefert die Instanz der Messages.
	 * 
	 * @return Das Interface der Texte.
	 */
	public static MatchServiceAsync get() {
		return Holder.matchService;
	}

}
