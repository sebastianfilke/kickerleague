package de.kickerapp.client.ui.controller.chart;

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
import de.kickerapp.client.ui.resources.icons.KickerIcons;

/**
 * Controller-Klasse zum Anzeigen der Team- bzw. Spielerstatistiken.
 * 
 * @author Sebastian Filke
 */
public class ChartTabPanel extends BasePanel implements ShowDataEventHandler {

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
	public void initLayout() {
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

		AppEventBus.addHandler(ShowDataEvent.CHART, this);
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

		final TabItemConfig ticSinglePlayerChart = new TabItemConfig("Einzelspielerstatistik");
		ticSinglePlayerChart.setIcon(KickerIcons.ICON.chart_bar());

		final TabItemConfig ticDoublePlayerChart = new TabItemConfig("Doppelspielerstatistik");
		ticDoublePlayerChart.setIcon(KickerIcons.ICON.chart_bar());

		tabPanel.add(singlePlayerChartPanel, ticSinglePlayerChart);
		tabPanel.add(doublePlayerChartPanel, ticDoublePlayerChart);
		tabPanel.setBodyBorder(false);
		tabPanel.setBorders(false);

		return tabPanel;
	}

	private void getData() {
		if (activeTab == 0) {
			singlePlayerChartPanel.getPlayerList();
		} else if (activeTab == 1) {
			doublePlayerChartPanel.getPlayerList();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showData(ShowDataEvent event) {
		getData();
	}

}
