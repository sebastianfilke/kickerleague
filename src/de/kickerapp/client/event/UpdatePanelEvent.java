package de.kickerapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class UpdatePanelEvent extends GwtEvent<UpdatePanelEventHandler> {

	public static final GwtEvent.Type<UpdatePanelEventHandler> ALL = new GwtEvent.Type<UpdatePanelEventHandler>();
	public static final GwtEvent.Type<UpdatePanelEventHandler> TABLES = new GwtEvent.Type<UpdatePanelEventHandler>();
	public static final GwtEvent.Type<UpdatePanelEventHandler> MATCHES = new GwtEvent.Type<UpdatePanelEventHandler>();

	/** Der Typ des Ereignisses. */
	private Type<UpdatePanelEventHandler> eventType;

	/**
	 * Erstellt ein neues <tt>NavigationsEvent</tt> mit angegebenem Ereignistyp.
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
		}
		return associatedType;
	}

}
