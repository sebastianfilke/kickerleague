package de.kickerapp.client.ui.controller.tab;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.MarginData;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.SelectNavigationElementEvent;
import de.kickerapp.client.event.ShowDataEvent;
import de.kickerapp.client.event.ShowDataEventHandler;
import de.kickerapp.client.event.TabPanelEvent;
import de.kickerapp.client.event.TabPanelEventHandler;
import de.kickerapp.client.ui.base.BasePanel;
import de.kickerapp.client.ui.controller.DoublePlayerChartPanel;
import de.kickerapp.client.ui.controller.SinglePlayerChartPanel;
import de.kickerapp.client.ui.controller.TeamChartPanel;
import de.kickerapp.client.ui.navigation.NavigationElement;
import de.kickerapp.client.ui.resources.IconProvider;

/**
 * Controller-Klasse zum Anzeigen der Team- bzw. Spielerstatistiken.
 * 
 * @author Sebastian Filke
 */
public class ChartTabPanel extends BasePanel implements ShowDataEventHandler, TabPanelEventHandler {

	/** Controller-Klasse für die Ansicht der Einzelstatistiken. */
	private SinglePlayerChartPanel singlePlayerChartPanel;
	/** Controller-Klasse für die Ansicht der Doppelstatistiken. */
	private DoublePlayerChartPanel doublePlayerChartPanel;
	/** Controller-Klasse für die Ansicht der Teamstatistiken. */
	private TeamChartPanel teamChartPanel;
	/** Der TabPanel zum Anzeigen der verschiedenen Statistiken. */
	private TabPanel tabPanel;
	/** Der aktuelle oder anzuzeigende Tab. */
	private int activeTab;

	/**
	 * Erzeugt einen neuen Controller zum Anzeigen der Team- bzw. Spielerstatistiken.
	 */
	public ChartTabPanel() {
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
		setHeadingHtml("<span id='panelHeading'>Team-/Spielerstatistiken</span>");

		activeTab = 0;

		singlePlayerChartPanel = new SinglePlayerChartPanel();
		doublePlayerChartPanel = new DoublePlayerChartPanel();
		teamChartPanel = new TeamChartPanel();

		tabPanel = createTabPanel();

		add(tabPanel, new MarginData(5, 0, 0, 0));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();

		AppEventBus.addHandler(ShowDataEvent.CHARTS, this);
		AppEventBus.addHandler(TabPanelEvent.CHARTS, this);
	}

	/**
	 * Erzeugt den TabPanel zum Anzeigen der verschiedenen Statistiken.
	 * 
	 * @return Der erzeugte TabPanel.
	 */
	private TabPanel createTabPanel() {
		final PlainTabPanel plainTabPanel = new PlainTabPanel();
		plainTabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				final TabPanel panel = (TabPanel) event.getSource();
				final Widget widget = event.getSelectedItem();

				activeTab = panel.getWidgetIndex(widget);
				getChartForActiveTab();
			}
		});
		plainTabPanel.setBodyBorder(false);
		plainTabPanel.setAutoSelect(false);
		plainTabPanel.setResizeTabs(true);
		plainTabPanel.setBorders(false);
		plainTabPanel.setTabWidth(200);

		final TabItemConfig ticSinglePlayerChart = new TabItemConfig("Einzelstatistik");
		ticSinglePlayerChart.setIcon(IconProvider.get().chart_bar());

		final TabItemConfig ticDoublePlayerChart = new TabItemConfig("Doppelstatistik");
		ticDoublePlayerChart.setIcon(IconProvider.get().chart_bar());

		final TabItemConfig ticTeamPlayerChart = new TabItemConfig("Teamstatistik");
		ticTeamPlayerChart.setIcon(IconProvider.get().chart_bar());

		plainTabPanel.add(singlePlayerChartPanel, ticSinglePlayerChart);
		plainTabPanel.add(doublePlayerChartPanel, ticDoublePlayerChart);
		plainTabPanel.add(teamChartPanel, ticTeamPlayerChart);
		plainTabPanel.setActiveWidget(singlePlayerChartPanel, false);

		return plainTabPanel;
	}

	/**
	 * Liefert die Team- bzw. Spielerstatistik für das aktuell aktive Tab.
	 */
	private void getChartForActiveTab() {
		if (activeTab == 0) {
			singlePlayerChartPanel.getPlayerList();
			singlePlayerChartPanel.updateSinglePlayer();
		} else if (activeTab == 1) {
			doublePlayerChartPanel.getPlayerList();
			doublePlayerChartPanel.updateDoublePlayer();
		} else if (activeTab == 2) {
			teamChartPanel.getTeamList();
			teamChartPanel.updateTeam();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showData(ShowDataEvent event) {
		final NavigationElement navigationElement = event.getNavigationElement();
		if (navigationElement != NavigationElement.UNKOWN) {
			activeTab = navigationElement.getTabIndex();
			tabPanel.setActiveWidget(tabPanel.getWidget(activeTab));
		}
		AppEventBus.fireEvent(new SelectNavigationElementEvent(SelectNavigationElementEvent.CHARTS, activeTab));
		getChartForActiveTab();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setActiveWidget(TabPanelEvent event) {
		activeTab = event.getTabIndex();
	}

}
