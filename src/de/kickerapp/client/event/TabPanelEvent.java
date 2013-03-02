package de.kickerapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class TabPanelEvent extends GwtEvent<TabPanelEventHandler> {

	public static final GwtEvent.Type<TabPanelEventHandler> TYPE = new GwtEvent.Type<TabPanelEventHandler>();

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

	public int getActiveTab() {
		return activeWidget;
	}

	public void setActiveWidget(int activeWidget) {
		this.activeWidget = activeWidget;
	}

}
