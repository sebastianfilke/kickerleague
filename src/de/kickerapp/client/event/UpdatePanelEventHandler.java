package de.kickerapp.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Schnittstelle zur Behandlung von Aktualisierungs-Ereignissen.
 * 
 * @author Sebastian Filke
 */
public interface UpdatePanelEventHandler extends EventHandler {

	/**
	 * Methode zur Verarbeitung von Ereignissen zum Aktualisieren.
	 * 
	 * @param event Das Aktualisierungs-Ereignis.
	 */
	public void updatePanel(UpdatePanelEvent event);

}
