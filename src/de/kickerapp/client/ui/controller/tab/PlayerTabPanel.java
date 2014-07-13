package de.kickerapp.client.ui.controller.tab;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.MarginData;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.ShowDataEvent;
import de.kickerapp.client.event.ShowDataEventHandler;
import de.kickerapp.client.ui.base.BasePanel;
import de.kickerapp.client.ui.controller.PlayerAdminPanel;
import de.kickerapp.client.ui.resources.IconProvider;

/**
 * Controller-Klasse zum Eintragen neuer Spieler für die Applikation.
 * 
 * @author Sebastian Filke
 */
public class PlayerTabPanel extends BasePanel implements ShowDataEventHandler {

	private PlayerAdminPanel playerAdminPanel;

	private TabPanel tabPanel;

	private int activeTab;

	/**
	 * Erzeugt einen neuen Controller zum Eintragen neuer Spieler für die Applikation.
	 */
	public PlayerTabPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initLayout() {
		super.initLayout();
		setHeadingHtml("<span id='panelHeading'>Spielerverwaltung</span>");

		activeTab = 0;

		playerAdminPanel = new PlayerAdminPanel();

		tabPanel = createTabPanel();

		add(tabPanel, new MarginData(5, 0, 0, 0));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();

		AppEventBus.addHandler(ShowDataEvent.PLAYER, this);
	}

	private TabPanel createTabPanel() {
		final PlainTabPanel tabPanel = new PlainTabPanel();
		tabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				final TabPanel panel = (TabPanel) event.getSource();
				final Widget widget = event.getSelectedItem();

				activeTab = panel.getWidgetIndex(widget);
				getData();
			}
		});
		tabPanel.setResizeTabs(true);
		tabPanel.setTabWidth(200);

		final TabItemConfig ticAdminPanel = new TabItemConfig("Spieler eintragen/bearbeiten");
		ticAdminPanel.setIcon(IconProvider.get().player_edit());

		tabPanel.add(playerAdminPanel, ticAdminPanel);
		tabPanel.setBodyBorder(false);
		tabPanel.setBorders(false);

		return tabPanel;
	}

	private void getData() {
		if (activeTab == 0) {
			playerAdminPanel.getPlayerList();
		} else if (activeTab == 1) {}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showData(ShowDataEvent event) {
		getData();
	}

}