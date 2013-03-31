package de.kickerapp.client.ui.util;

import com.sencha.gxt.widget.core.client.info.DefaultInfoConfig;
import com.sencha.gxt.widget.core.client.info.Info;

/**
 * Hilfsklasse zum Anzeigen von Infonachrichten der Applikation.
 * 
 * @author Sebastian Filke
 */
public final class AppInfo {

	/**
	 * Konstruktor soll von Außen nicht aufgerufen werden.
	 */
	private AppInfo() {
	}

	/**
	 * Zeigt eine Nachricht mit übergebenem Titel und Text.
	 * 
	 * @param title Der Titel als {@link String}.
	 * @param message Die Nachricht als {@link String}.
	 */
	public static void showInfo(String title, String message) {
		Info.display(new AppInfoConfig(title, message));
	}

	/**
	 * Innere Klasse für die Konfiguration der Infonachrichten.
	 * 
	 * @author Sebastian Filke
	 */
	private static class AppInfoConfig extends DefaultInfoConfig {

		/**
		 * Erzeugt eine Nachricht mit übergebenem Titel und Text.
		 * 
		 * @param title Der Titel als {@link String}.
		 * @param message Die Nachricht als {@link String}.
		 */
		public AppInfoConfig(String title, String message) {
			super(title, message);
			setDisplay(8000);
		}
	}

}
