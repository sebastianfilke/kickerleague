package de.kickerapp.client.ui;

import de.kickerapp.client.widgets.AppContentPanel;

/**
 * Basis-Controller zur Darstellung und Verarbeitung der Applikation.
 * 
 * @author Sebastian Filke
 */
public class BasePanel extends AppContentPanel {

	/**
	 * Erzeugt einen neuen Basis-Controller.
	 */
	public BasePanel() {
		initLayout();
	}

	/**
	 * Initalisiert das Layout des Panels.
	 */
	public void initLayout() {
		setHeaderVisible(false);
	}

	/**
	 * Initalisiert die EventHandler des Panels.
	 */
	public void initHandlers() {
	}

}
