package de.kickerapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Definition der Anzeige-Ereignisse für TabPanels.
 * 
 * @author Sebastian Filke
 */
public class TabPanelEvent extends GwtEvent<TabPanelEventHandler> {

	/** Das Ereignis zum Ändern des TabPanels bei den Tabellen. */
	public static final GwtEvent.Type<TabPanelEventHandler> TABLES = new GwtEvent.Type<TabPanelEventHandler>();
	/** Das Ereignis zum Ändern des selektierten Elements in der Navigationsleiste. */
	public static final GwtEvent.Type<TabPanelEventHandler> TABLES_NAV = new GwtEvent.Type<TabPanelEventHandler>();
	/** Das Ereignis zum Ändern des TabPanels bei den Statistiken. */
	public static final GwtEvent.Type<TabPanelEventHandler> CHARTS = new GwtEvent.Type<TabPanelEventHandler>();
	/** Das Ereignis zum Ändern des selektierten Elements in der Navigationsleiste. */
	public static final GwtEvent.Type<TabPanelEventHandler> CHARTS_NAV = new GwtEvent.Type<TabPanelEventHandler>();

	/** Der Typ des Ereignisses. */
	private Type<TabPanelEventHandler> eventType;
	/** Die Nummer des Tabs, welches angezeigt werden soll. */
	private int activeTab;

	/**
	 * Erstellt ein neues TabPanelEvent mit übergebener Nummer eines Tabs.
	 * 
	 * @param eventType Der Typ des Ereignisses.
	 * @param activeTab Die Nummer des Tabs, welches angezeigt werden soll als {@link Integer}.
	 */
	public TabPanelEvent(Type<TabPanelEventHandler> eventType, int activeTab) {
		this.eventType = eventType;
		this.activeTab = activeTab;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void dispatch(TabPanelEventHandler handler) {
		handler.setActiveWidget(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type<TabPanelEventHandler> getAssociatedType() {
		Type<TabPanelEventHandler> associatedType = null;
		if (eventType == TABLES) {
			associatedType = TABLES;
		} else if (eventType == TABLES_NAV) {
			associatedType = TABLES_NAV;
		} else if (eventType == CHARTS) {
			associatedType = CHARTS;
		} else if (eventType == CHARTS_NAV) {
			associatedType = CHARTS_NAV;
		}
		return associatedType;
	}

	/**
	 * Liefer die Nummer des Tabs, welches angezeigt werden soll.
	 * 
	 * @return Die Nummer des Tabs, welches angezeigt werden soll als <code>Integer</code>.
	 */
	public int getActiveTab() {
		return activeTab;
	}

	/**
	 * Setzt die Nummer des Tabs, welches angezeigt werden soll.
	 * 
	 * @param activeTab Die Nummer des Tabs, welches angezeigt werden soll als <code>Integer</code>.
	 */
	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

}
