package de.kickerapp.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface UpdatePanelEventHandler extends EventHandler {

	/**
	 * Methode zur Verarbeitung von Ereignissen zum Sprachwechsel der
	 * Applikation.
	 */
	public void updatePanel(UpdatePanelEvent event);
	
}
