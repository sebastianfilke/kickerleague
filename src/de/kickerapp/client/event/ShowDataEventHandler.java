package de.kickerapp.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Schnittstelle zur Behandlung von Datenanzeige-Ereignissen.
 * 
 * @author Sebastian Filke
 */
public interface ShowDataEventHandler extends EventHandler {

	/**
	 * Methode zur Verarbeitung von Datenanzeige-Ereignisse.
	 * 
	 * @param event Das Datenanzeige-Ereignis.
	 */
	void showData(ShowDataEvent event);

}
