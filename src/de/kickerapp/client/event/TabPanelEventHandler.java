package de.kickerapp.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Schnittstelle zur Behandlung von Anzeige-Ereignisse für TabPanels.
 * 
 * @author Sebastian Filke
 */
public interface TabPanelEventHandler extends EventHandler {

	/**
	 * Methode zur Verarbeitung von Anzeige-Ereignisse für TabPanels.
	 * 
	 * @param event Das Anzeige-Ereignis.
	 */
	void setActiveWidget(TabPanelEvent event);

}
