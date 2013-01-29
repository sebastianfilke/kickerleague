package de.kickerapp.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Schnittstelle zur Behandlung von Ereignissen zum Sprachwechsel der
 * Applikation.
 * 
 * @author Sebastian Filke
 */
public interface UpdatePanelEventHandler extends EventHandler {

	/**
	 * Methode zur Verarbeitung von Ereignissen zum Sprachwechsel der
	 * Applikation.
	 */
	public void updatePanel();

}
