package de.kickerapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Definition der Sprachwechsel-Ereignisse der Applikation.
 * 
 * @author Sebastian Filke
 */
public class ShowDataEvent extends GwtEvent<ShowDataEventHandler> {

	public static final GwtEvent.Type<ShowDataEventHandler> TABLES = new GwtEvent.Type<ShowDataEventHandler>();
	public static final GwtEvent.Type<ShowDataEventHandler> MATCHES = new GwtEvent.Type<ShowDataEventHandler>();
	public static final GwtEvent.Type<ShowDataEventHandler> INSERT = new GwtEvent.Type<ShowDataEventHandler>();

	/** Der Typ des Ereignisses. */
	private Type<ShowDataEventHandler> eventType;

	/**
	 * Erstellt ein neues <tt>AfterLayoutEvent</tt> mit angegebenem Ereignistyp.
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
		}
		return associatedType;
	}

}
