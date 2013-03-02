package de.kickerapp.client.ui;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.MarginData;

/**
 * Controller-Klasse zum Eintragen neuer Spieler für die Applikation.
 * 
 * @author Sebastian Filke
 */
public class PlayerTabPanel extends BasePanel {

	private PlayerAdminPanel playerAdminPanel;

	private TabPanel tabPanel;

	private int activeTab;

	/**
	 * Erzeugt einen neuen Controller zum Eintragen neuer Spieler für die
	 * Applikation.
	 */
	public PlayerTabPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		super.initLayout();
		setHeadingHtml("<span id='panelHeading'>Spielerstatistiken/-verwaltung</span>");

		activeTab = 0;

		playerAdminPanel = new PlayerAdminPanel();

		tabPanel = createTabPanel();

		add(tabPanel, new MarginData(5, 0, 0, 0));
	}

	private TabPanel createTabPanel() {
		final PlainTabPanel tabPanel = new PlainTabPanel();
		tabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				final TabPanel panel = (TabPanel) event.getSource();
				final Widget w = event.getSelectedItem();

				activeTab = panel.getWidgetIndex(w);
			}
		});
		tabPanel.setResizeTabs(true);
		tabPanel.setTabWidth(200);

		tabPanel.add(playerAdminPanel, "Spieler eintragen/bearbeiten");
		tabPanel.setBodyBorder(false);
		tabPanel.setBorders(false);

		return tabPanel;
	}

}
