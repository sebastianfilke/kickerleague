package de.kickerapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Definition der Navigations-Ereignisse.
 * 
 * @author Sebastian Filke
 */
public class NavigationEvent extends GwtEvent<NavigationEventHandler> {

	/** Das Ereignis zum Anzeigen der Tabellen. */
	public static final GwtEvent.Type<NavigationEventHandler> TABLES = new GwtEvent.Type<NavigationEventHandler>();
	/** Das Ereignis zum Anzeigen der Ergebnisse. */
	public static final GwtEvent.Type<NavigationEventHandler> MATCHES = new GwtEvent.Type<NavigationEventHandler>();
	/** Das Ereignis zum Anzeigen des Panels zum Eintragen von Ergebnissen. */
	public static final GwtEvent.Type<NavigationEventHandler> INSERT = new GwtEvent.Type<NavigationEventHandler>();
	/** Das Ereignis zum Anzeigen des Panels zum Anzeigen der Team- bzw. Spielerstatistiken. */
	public static final GwtEvent.Type<NavigationEventHandler> CHART = new GwtEvent.Type<NavigationEventHandler>();
	/** Das Ereignis zum Anzeigen der Spieler und Spielerstatistiken. */
	public static final GwtEvent.Type<NavigationEventHandler> PLAYER = new GwtEvent.Type<NavigationEventHandler>();

	/** Der Typ des Ereignisses. */
	private Type<NavigationEventHandler> eventType;

	/**
	 * Erstellt ein neues <tt>NavigationsEvent</tt> mit angegebenem Ereignistyp.
	 * 
	 * @param theEventType Der Typ des Ereignisses.
	 */
	public NavigationEvent(Type<NavigationEventHandler> theEventType) {
		eventType = theEventType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void dispatch(NavigationEventHandler handler) {
		handler.navigationPressed(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type<NavigationEventHandler> getAssociatedType() {
		Type<NavigationEventHandler> associatedType = null;
		if (eventType == TABLES) {
			associatedType = TABLES;
		} else if (eventType == MATCHES) {
			associatedType = MATCHES;
		} else if (eventType == INSERT) {
			associatedType = INSERT;
		} else if (eventType == CHART) {
			associatedType = CHART;
		} else if (eventType == PLAYER) {
			associatedType = PLAYER;
		}
		return associatedType;
	}

}
