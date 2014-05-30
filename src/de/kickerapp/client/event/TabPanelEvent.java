package de.kickerapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Definition der Anzeige-Ereignisse für TabPanels.
 * 
 * @author Sebastian Filke
 */
public class TabPanelEvent extends GwtEvent<TabPanelEventHandler> {

	/** Das Ereignis zum Anzeigen der TabPanels. */
	public static final GwtEvent.Type<TabPanelEventHandler> TYPE = new GwtEvent.Type<TabPanelEventHandler>();

	/** Die Nummer des Tabs, welches angezeigt werden soll. */
	private int activeWidget;

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
		return TYPE;
	}

	/**
	 * Liefer die Nummer des Tabs, welches angezeigt werden soll.
	 * 
	 * @return Die Nummer des Tabs, welches angezeigt werden soll als <code>Integer</code>.
	 */
	public int getActiveTab() {
		return activeWidget;
	}

	/**
	 * Setzt die Nummer des Tabs, welches angezeigt werden soll.
	 * 
	 * @param activeWidget Die Nummer des Tabs, welches angezeigt werden soll als <code>Integer</code>.
	 */
	public void setActiveWidget(int activeWidget) {
		this.activeWidget = activeWidget;
	}

}
