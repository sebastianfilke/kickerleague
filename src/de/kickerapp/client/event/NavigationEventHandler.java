package de.kickerapp.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Schnittstelle zur Behandlung von Navigations-Ereignissen.
 * 
 * @author Sebastian Filke
 */
public interface NavigationEventHandler extends EventHandler {

    /**
     * Methode zur Verarbeitung von Navigations-Ereignissen.
     * 
     * @param event Das Navigations-Ereignis.
     */
    public void navigationPressed(NavigationEvent event);

}
