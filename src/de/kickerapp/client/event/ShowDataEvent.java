package de.kickerapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Definition der Sprachwechsel-Ereignisse der Applikation.
 * 
 * @author Sebastian Filke
 */
public class ShowDataEvent extends GwtEvent<ShowDataEventHandler> {

	/** Das Sprachwechsel-Ereignis. */
	public static final GwtEvent.Type<ShowDataEventHandler> ALL_PANEL = new GwtEvent.Type<ShowDataEventHandler>();
	/** Das Sprachwechsel-Ereignis. */
	public static final GwtEvent.Type<ShowDataEventHandler> MATCHES_PANEL = new GwtEvent.Type<ShowDataEventHandler>();
	/** Das Sprachwechsel-Ereignis. */
	public static final GwtEvent.Type<ShowDataEventHandler> TABLE_PANEL = new GwtEvent.Type<ShowDataEventHandler>();

	/** Der Typ des Ereignisses. */
	private Type<ShowDataEventHandler> eventType;

	private int activeWidget;

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
		if (eventType == ALL_PANEL) {
			associatedType = ALL_PANEL;
		} else if (eventType == MATCHES_PANEL) {
			associatedType = MATCHES_PANEL;
		} else if (eventType == TABLE_PANEL) {
			associatedType = TABLE_PANEL;
		}
		return associatedType;
	}

	public int getActiveWidget() {
		return activeWidget;
	}

	public void setActiveWidget(int activeWidget) {
		this.activeWidget = activeWidget;
	}

}
