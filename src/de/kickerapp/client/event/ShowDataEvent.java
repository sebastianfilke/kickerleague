package de.kickerapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Definition der Datenanzeige-Ereignisse.
 * 
 * @author Sebastian Filke
 */
public class ShowDataEvent extends GwtEvent<ShowDataEventHandler> {

	/** Das Ereignis zum Anzeigen der Daten der Tabellen. */
	public static final GwtEvent.Type<ShowDataEventHandler> TABLES = new GwtEvent.Type<ShowDataEventHandler>();
	/** Das Ereignis zum Anzeigen der Daten der Ergebnisse. */
	public static final GwtEvent.Type<ShowDataEventHandler> MATCHES = new GwtEvent.Type<ShowDataEventHandler>();
	/** Das Ereignis zum Anzeigen der Daten für den Panel zum Eintragen von Ergebnissen. */
	public static final GwtEvent.Type<ShowDataEventHandler> INSERT = new GwtEvent.Type<ShowDataEventHandler>();
	/** Das Ereignis zum Anzeigen der Daten für den Panel zum Anzeigen der Team- bzw. Spielerstatistiken. */
	public static final GwtEvent.Type<ShowDataEventHandler> CHART = new GwtEvent.Type<ShowDataEventHandler>();
	/** Das Ereignis zum Anzeigen der Daten der Spieler und Spielerstatistiken. */
	public static final GwtEvent.Type<ShowDataEventHandler> PLAYER = new GwtEvent.Type<ShowDataEventHandler>();

	/** Der Typ des Ereignisses. */
	private Type<ShowDataEventHandler> eventType;

	/**
	 * Erstellt ein neues <tt>ShowDataEvent</tt> mit angegebenem Ereignistyp.
	 * 
	 * @param theEventType Der Typ des Ereignisses.
	 */
	public ShowDataEvent(Type<ShowDataEventHandler> theEventType) {
		eventType = theEventType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void dispatch(ShowDataEventHandler handler) {
		handler.showData(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type<ShowDataEventHandler> getAssociatedType() {
		Type<ShowDataEventHandler> associatedType = null;
		if (eventType == MATCHES) {
			associatedType = MATCHES;
		} else if (eventType == TABLES) {
			associatedType = TABLES;
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
