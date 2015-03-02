package de.kickerapp.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Schnittstelle zur Behandlung von NavigationsElement-Ereignissen.
 * 
 * @author Sebastian Filke
 */
public interface SelectNavigationElementEventHandler extends EventHandler {

	/**
	 * Methode zur Verarbeitung von NavigationsElement-Ereignissen.
	 * 
	 * @param event Das NavigationsElement-Ereignis.
	 */
	void selectElement(SelectNavigationElementEvent event);

}
