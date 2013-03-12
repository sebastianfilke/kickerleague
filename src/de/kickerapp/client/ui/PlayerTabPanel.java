package de.kickerapp.client.ui;

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
import de.kickerapp.client.ui.images.KickerIcons;

/**
 * Controller-Klasse zum Eintragen neuer Spieler für die Applikation.
 * 
 * @author Sebastian Filke
 */
public class PlayerTabPanel extends BasePanel implements ShowDataEventHandler {

	private PlayerAdminPanel playerAdminPanel;

	// private SinglePlayerChartPanel chartPanel;

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
	public void initLayout() {
		super.initLayout();
		setHeadingHtml("<span id='panelHeading'>Spielerstatistiken/-verwaltung</span>");

		activeTab = 0;

		playerAdminPanel = new PlayerAdminPanel();
		// chartPanel = new SinglePlayerChartPanel();

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
				final Widget w = event.getSelectedItem();

				activeTab = panel.getWidgetIndex(w);
				tabPanel.forceLayout();
				getData();
			}
		});
		tabPanel.setResizeTabs(true);
		tabPanel.setTabWidth(200);

		final TabItemConfig ticSinglePlayerChart = new TabItemConfig("Einzelspielerstatistik");
		ticSinglePlayerChart.setIcon(KickerIcons.ICON.chart_bar());

		final TabItemConfig ticAdminPanel = new TabItemConfig("Spieler eintragen/bearbeiten");
		ticAdminPanel.setIcon(KickerIcons.ICON.user_edit());

		// tabPanel.add(chartPanel, ticSinglePlayerChart);
		tabPanel.add(playerAdminPanel, ticAdminPanel);
		tabPanel.setBodyBorder(false);
		tabPanel.setBorders(false);

		return tabPanel;
	}

	private void getData() {
		// if (activeTab == 0) {
		// chartPanel.getPlayerList();
		// } else if (activeTab == 1) {
		playerAdminPanel.getPlayerList();
		// }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showData(ShowDataEvent event) {
		getData();
	}

}
