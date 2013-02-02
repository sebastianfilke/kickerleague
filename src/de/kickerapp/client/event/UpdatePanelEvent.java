package de.kickerapp.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class UpdatePanelEvent extends GwtEvent<UpdatePanelEventHandler> {

	/** Das Sprachwechsel-Ereignis. */
	public static final GwtEvent.Type<UpdatePanelEventHandler> TYPE = new GwtEvent.Type<UpdatePanelEventHandler>();

	private int activeWidget;

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
		return TYPE;
	}

	public int getActiveWidget() {
		return activeWidget;
	}

	public void setActiveWidget(int activeWidget) {
		this.activeWidget = activeWidget;
	}

}
