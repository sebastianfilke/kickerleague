package de.kickerapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

import de.kickerapp.client.ui.navigation.NavigationElement;

/**
 * Definition der Datenanzeige-Ereignisse.
 * 
 * @author Sebastian Filke
 */
public class ShowDataEvent extends GwtEvent<ShowDataEventHandler> {

	/** Das Ereignis zum Anzeigen der Daten der Tabellen. */
	public static final GwtEvent.Type<ShowDataEventHandler> TABLES = new GwtEvent.Type<ShowDataEventHandler>();
	/** Das Ereignis zum Anzeigen der Daten der Ergebnisse. */
	public static final GwtEvent.Type<ShowDataEventHandler> RESULTS = new GwtEvent.Type<ShowDataEventHandler>();
	/** Das Ereignis zum Anzeigen der Daten für den Panel zum Eintragen von Ergebnissen. */
	public static final GwtEvent.Type<ShowDataEventHandler> INSERT = new GwtEvent.Type<ShowDataEventHandler>();
	/** Das Ereignis zum Anzeigen der Daten für den Panel zum Anzeigen der Team- bzw. Spielerstatistiken. */
	public static final GwtEvent.Type<ShowDataEventHandler> CHARTS = new GwtEvent.Type<ShowDataEventHandler>();
	/** Das Ereignis zum Anzeigen der Daten der Spieler und Spielerstatistiken. */
	public static final GwtEvent.Type<ShowDataEventHandler> PLAYER = new GwtEvent.Type<ShowDataEventHandler>();

	/** Der Typ des Ereignisses. */
	private Type<ShowDataEventHandler> eventType;
	/** Das zu selektierende Element der Navigationsleiste. */
	private NavigationElement navigationElement;

	/**
	 * Erstellt ein neues <tt>ShowDataEvent</tt> mit angegebenem Ereignistyp.
	 * 
	 * @param eventType Der Typ des Ereignisses.
	 */
	public ShowDataEvent(Type<ShowDataEventHandler> eventType) {
		this.eventType = eventType;
		navigationElement = NavigationElement.UNKOWN;
	}

	/**
	 * Erstellt ein neues <tt>ShowDataEvent</tt> mit angegebenem Ereignistyp.
	 * 
	 * @param eventType Der Typ des Ereignisses.
	 * @param navigationElement Das zu selektierende Element der Navigationsleiste.
	 */
	public ShowDataEvent(Type<ShowDataEventHandler> eventType, NavigationElement navigationElement) {
		this.eventType = eventType;
		this.navigationElement = navigationElement;
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
		if (eventType == RESULTS) {
			associatedType = RESULTS;
		} else if (eventType == TABLES) {
			associatedType = TABLES;
		} else if (eventType == INSERT) {
			associatedType = INSERT;
		} else if (eventType == CHARTS) {
			associatedType = CHARTS;
		} else if (eventType == PLAYER) {
			associatedType = PLAYER;
		}
		return associatedType;
	}

	/**
	 * Liefer das zu selektierende Element der Navigationsleiste.
	 * 
	 * @return Das zu selektierende Element der Navigationsleiste, welches angezeigt werden soll als <code>NavigationElement</code>.
	 */
	public NavigationElement getNavigationElement() {
		return navigationElement;
	}

}
