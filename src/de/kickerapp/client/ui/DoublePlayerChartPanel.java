package de.kickerapp.client.ui;

import de.kickerapp.client.event.UpdatePanelEvent;
import de.kickerapp.client.event.UpdatePanelEventHandler;
import de.kickerapp.client.ui.base.BaseContainer;

public class DoublePlayerChartPanel extends BaseContainer implements UpdatePanelEventHandler {

	public DoublePlayerChartPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePanel(UpdatePanelEvent event) {

	}

}
