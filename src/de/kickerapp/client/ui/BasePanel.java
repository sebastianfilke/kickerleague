package de.kickerapp.client.ui;

import com.sencha.gxt.widget.core.client.Portlet;

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
	protected void initLayout() {
		setHeaderVisible(false);
	}

	/**
	 * Initalisiert die EventHandler des Panels.
	 */
	protected void initHandlers() {
	}

	/**
	 * Initalisiert die Aktions-Buttons für die Portale.
	 * 
	 * @param portlet Das Portal für das die Aktions-Buttons initalisiert werden
	 *            sollen.
	 */
	protected void initPanelButtons(Portlet portlet) {
	}

}
