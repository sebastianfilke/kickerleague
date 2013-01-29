package de.kickerapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Definition der Sprachwechsel-Ereignisse der Applikation.
 * 
 * @author Sebastian Filke
 */
public class UpdatePanelEvent extends GwtEvent<UpdatePanelEventHandler> {

	/** Das Sprachwechsel-Ereignis. */
	public static final GwtEvent.Type<UpdatePanelEventHandler> ALL_PANEL = new GwtEvent.Type<UpdatePanelEventHandler>();
	/** Das Sprachwechsel-Ereignis. */
	public static final GwtEvent.Type<UpdatePanelEventHandler> MATCHES_PANEL = new GwtEvent.Type<UpdatePanelEventHandler>();
	/** Das Sprachwechsel-Ereignis. */
	public static final GwtEvent.Type<UpdatePanelEventHandler> TABLE_PANEL = new GwtEvent.Type<UpdatePanelEventHandler>();

	/** Der Typ des Ereignisses. */
	private Type<UpdatePanelEventHandler> eventType;

	/**
	 * Erstellt ein neues <tt>AfterLayoutEvent</tt> mit angegebenem Ereignistyp.
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
		handler.updatePanel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type<UpdatePanelEventHandler> getAssociatedType() {
		Type<UpdatePanelEventHandler> associatedType = null;
		if (eventType == ALL_PANEL) {
			associatedType = ALL_PANEL;
		} else if (eventType == MATCHES_PANEL) {
			associatedType = MATCHES_PANEL;
		} else if (eventType == TABLE_PANEL) {
			associatedType = TABLE_PANEL;
		}
		return associatedType;
	}

}
