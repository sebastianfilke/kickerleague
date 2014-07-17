package de.kickerapp.client.ui.controller.tab;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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
import de.kickerapp.client.event.TabPanelEvent;
import de.kickerapp.client.event.TabPanelEventHandler;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.client.ui.base.BasePanel;
import de.kickerapp.client.ui.controller.DoublePlayerChartPanel;
import de.kickerapp.client.ui.controller.SinglePlayerChartPanel;
import de.kickerapp.client.ui.resources.IconProvider;

/**
 * Controller-Klasse zum Anzeigen der Team- bzw. Spielerstatistiken.
 * 
 * @author Sebastian Filke
 */
public class ChartTabPanel extends BasePanel implements ShowDataEventHandler, TabPanelEventHandler {

	private SinglePlayerChartPanel singlePlayerChartPanel;

	private DoublePlayerChartPanel doublePlayerChartPanel;

	private TabPanel tabPanel;

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

	private TabPanel createTabPanel() {
		final PlainTabPanel tabPanel = new PlainTabPanel();
		tabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				final TabPanel panel = (TabPanel) event.getSource();
				final Widget widget = event.getSelectedItem();

				activeTab = panel.getWidgetIndex(widget);
				getChartForActiveTab();
			}
		});
		tabPanel.setBodyBorder(false);
		tabPanel.setAutoSelect(false);
		tabPanel.setResizeTabs(true);
		tabPanel.setBorders(false);
		tabPanel.setTabWidth(200);

		final TabItemConfig ticSinglePlayerChart = new TabItemConfig("Einzelstatistik");
		ticSinglePlayerChart.setIcon(IconProvider.get().chart_bar());

		final TabItemConfig ticDoublePlayerChart = new TabItemConfig("Doppelstatistik");
		ticDoublePlayerChart.setIcon(IconProvider.get().chart_bar());

		final TabItemConfig ticTeamPlayerChart = new TabItemConfig("Teamstatistik");
		ticTeamPlayerChart.setIcon(IconProvider.get().chart_bar());

		tabPanel.add(singlePlayerChartPanel, ticSinglePlayerChart);
		tabPanel.add(doublePlayerChartPanel, ticDoublePlayerChart);
		tabPanel.add(new BaseContainer(), ticTeamPlayerChart);
		tabPanel.setActiveWidget(singlePlayerChartPanel, false);

		return tabPanel;
	}

	private void getChartForActiveTab() {
		if (activeTab == 0) {
			singlePlayerChartPanel.getPlayerList();
			singlePlayerChartPanel.updateSinglePlayer();
		} else if (activeTab == 1) {
			doublePlayerChartPanel.getPlayerList();
		}
		AppEventBus.fireEvent(new TabPanelEvent(TabPanelEvent.CHARTS_NAV, activeTab));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showData(ShowDataEvent event) {
		getChartForActiveTab();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setActiveWidget(TabPanelEvent event) {
		if (activeTab != event.getActiveTab()) {
			activeTab = event.getActiveTab();
			Scheduler.get().scheduleFinally(new ScheduledCommand() {
				@Override
				public void execute() {
					tabPanel.setActiveWidget(tabPanel.getWidget(activeTab));
				}
			});
		}
	}

}
