package de.kickerapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Definition der Aktualisierungs-Ereignisse.
 * 
 * @author Sebastian Filke
 */
public class UpdatePanelEvent extends GwtEvent<UpdatePanelEventHandler> {

	/** Das Ereignis zum Aktualisieren der Tabellen. */
	public static final GwtEvent.Type<UpdatePanelEventHandler> ALL = new GwtEvent.Type<UpdatePanelEventHandler>();
	/** Das Ereignis zum Aktualisieren der Ergebnisse. */
	public static final GwtEvent.Type<UpdatePanelEventHandler> TABLES = new GwtEvent.Type<UpdatePanelEventHandler>();
	/** Das Ereignis zum Aktualisieren des Panels zum Eintragen von Ergebnissen. */
	public static final GwtEvent.Type<UpdatePanelEventHandler> MATCHES = new GwtEvent.Type<UpdatePanelEventHandler>();
	/** Das Ereignis zum Aktualisieren der Spielerstatistiken. */
	public static final GwtEvent.Type<UpdatePanelEventHandler> CHARTS = new GwtEvent.Type<UpdatePanelEventHandler>();

	/** Der Typ des Ereignisses. */
	private Type<UpdatePanelEventHandler> eventType;

	/**
	 * Erstellt ein neues <tt>UpdatePanelEvent</tt> mit angegebenem Ereignistyp.
	 * 
	 * @param theEventType Der Typ des Ereignisses.
	 */
	public UpdatePanelEvent(Type<UpdatePanelEventHandler> theEventType) {
		eventType = theEventType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void dispatch(UpdatePanelEventHandler handler) {
		handler.updatePanel(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type<UpdatePanelEventHandler> getAssociatedType() {
		Type<UpdatePanelEventHandler> associatedType = null;
		if (eventType == ALL) {
			associatedType = ALL;
		} else if (eventType == TABLES) {
			associatedType = TABLES;
		} else if (eventType == MATCHES) {
			associatedType = MATCHES;
		} else if (eventType == CHARTS) {
			associatedType = CHARTS;
		}
		return associatedType;
	}

}
