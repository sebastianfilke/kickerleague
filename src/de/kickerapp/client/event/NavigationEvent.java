package de.kickerapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Definition der Navigations-Ereignisse.
 * 
 * @author Sebastian Filke, GIGATRONIK München GmbH
 */
public class NavigationEvent extends GwtEvent<NavigationEventHandler> {

	/** Das Arbeitsvorrats-Ereignis. */
	public static final GwtEvent.Type<NavigationEventHandler> TABLE = new GwtEvent.Type<NavigationEventHandler>();
	/** Das Auftragsdaten-Ereignis. */
	public static final GwtEvent.Type<NavigationEventHandler> RESULT = new GwtEvent.Type<NavigationEventHandler>();
	/** Das Prüflings-Ereignis. */
	public static final GwtEvent.Type<NavigationEventHandler> INPUT = new GwtEvent.Type<NavigationEventHandler>();
	/** Das Prüflingskombination-Ereignis. */
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
		if (eventType == TABLE) {
			associatedType = TABLE;
		} else if (eventType == RESULT) {
			associatedType = RESULT;
		} else if (eventType == INPUT) {
			associatedType = INPUT;
		} else if (eventType == PLAYER) {
			associatedType = PLAYER;
		}
		return associatedType;
	}

}
