package de.kickerapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

import de.kickerapp.client.ui.navigation.NavigationElement;

/**
 * Definition der NavigationsElement-Ereignisse.
 * 
 * @author Sebastian Filke
 */
public class SelectNavigationElementEvent extends GwtEvent<SelectNavigationElementEventHandler> {

	/** Das Ereignis zum Anzeigen des NavigationsElementes bei Tabellen. */
	public static final GwtEvent.Type<SelectNavigationElementEventHandler> TABLES = new GwtEvent.Type<SelectNavigationElementEventHandler>();
	/** Das Ereignis zum Anzeigen des NavigationsElementes bei Statistiken. */
	public static final GwtEvent.Type<SelectNavigationElementEventHandler> CHARTS = new GwtEvent.Type<SelectNavigationElementEventHandler>();

	/** Der Typ des Ereignisses. */
	private Type<SelectNavigationElementEventHandler> eventType;
	/** Die Nummer des Tabs, welches angezeigt werden soll. */
	private int tabIndex;

	/**
	 * Erstellt ein neues <tt>SelectNavigationElementEvent</tt> mit angegebenem Ereignistyp.
	 * 
	 * @param eventType Der Typ des Ereignisses.
	 * @param tabIndex Die Nummer des Tabs, welches angezeigt werden soll als {@link Integer}.
	 */
	public SelectNavigationElementEvent(Type<SelectNavigationElementEventHandler> eventType, int tabIndex) {
		this.eventType = eventType;
		this.tabIndex = tabIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void dispatch(SelectNavigationElementEventHandler handler) {
		handler.selectElement(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type<SelectNavigationElementEventHandler> getAssociatedType() {
		Type<SelectNavigationElementEventHandler> associatedType = null;
		if (eventType == TABLES) {
			associatedType = TABLES;
		} else if (eventType == CHARTS) {
			associatedType = CHARTS;
		}
		return associatedType;
	}

	/**
	 * Liefer das zu selektierende Element der Navigationsleiste.
	 * 
	 * @return Das zu selektierende Element der Navigationsleiste, welches angezeigt werden soll als <code>NavigationElement</code>.
	 */
	public NavigationElement getNavigationElement() {
		NavigationElement navigationElement = NavigationElement.UNKOWN;

		if (eventType == TABLES) {
			if (tabIndex == 0) {
				navigationElement = NavigationElement.SINGLETABLE;
			} else if (tabIndex == 1) {
				navigationElement = NavigationElement.DOUBLETABLE;
			} else if (tabIndex == 2) {
				navigationElement = NavigationElement.TEAMTABLE;
			}
		} else if (eventType == CHARTS) {
			if (tabIndex == 0) {
				navigationElement = NavigationElement.SINGLECHART;
			} else if (tabIndex == 1) {
				navigationElement = NavigationElement.DOUBLECHART;
			} else if (tabIndex == 2) {
				navigationElement = NavigationElement.TEAMCHART;
			}
		}
		return navigationElement;
	}

}
