package de.kickerapp.client.ui;

import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;

import de.kickerapp.client.ui.resources.KickerMessages;
import de.kickerapp.client.widgets.AppContentPanel;

/**
 * Basis-Controller zur Darstellung und Verarbeitung der Applikation.
 * 
 * @author Sebastian Filke
 */
public class MainPanel extends BasePanel {

	private ResultPanel resultPanel;

	private MatchesPanel matchesPanel;

	private PlayerPanel playerPanel;

	private TablePanel tablePanel;

	public MainPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		setHeaderVisible(false);
		setBodyBorder(false);

		final AppContentPanel cpWest = new AppContentPanel();

		final BorderLayoutContainer con = new BorderLayoutContainer();
		con.setBorders(false);

		final PortalPanel plCenter = new PortalPanel();
		initPanels(plCenter);

		final BorderLayoutData northData = new BorderLayoutData(80);

		final BorderLayoutData westData = new BorderLayoutData(280);
		westData.setCollapsible(true);
		westData.setSplit(true);
		westData.setCollapseMini(true);
		westData.setMargins(new Margins(5));

		final MarginData centerData = new MarginData();
		centerData.setMargins(new Margins(5, 5, 5, 0));

		final BorderLayoutData southData = new BorderLayoutData(45);

		con.setNorthWidget(createHeader(), northData);
		con.setWestWidget(cpWest, westData);
		con.setCenterWidget(plCenter, centerData);
		con.setSouthWidget(createFooter(), southData);

		add(con);
	}

	private HtmlLayoutContainer createHeader() {
		final StringBuilder sb = new StringBuilder();
		sb.append("<span id='headerTitle'>" + KickerMessages.MAIN_PANEL.mainPanelTitle() + "</span>");

		final HtmlLayoutContainer htmlLcHeader = new HtmlLayoutContainer(sb.toString());
		htmlLcHeader.setId("headerBackground");
		htmlLcHeader.setStateful(false);

		return htmlLcHeader;
	}

	private HtmlLayoutContainer createFooter() {
		final StringBuffer sb = new StringBuffer();
		sb.append("<span id='headerTitle'></span>");

		final HtmlLayoutContainer htmlLcFooter = new HtmlLayoutContainer(sb.toString());
		htmlLcFooter.setId("headerBackground");
		htmlLcFooter.setStateful(false);

		return htmlLcFooter;
	}

	/**
	 * @param portalPanel
	 */
	private void initPanels(PortalPanel portalPanel) {
		// Ergebnispanel
		resultPanel = new ResultPanel();
		resultPanel.initPanelButtons(portalPanel.getPortletResult());
		portalPanel.getPortletResult().add(resultPanel);

		// Spielergebnispanel
		matchesPanel = new MatchesPanel();
		matchesPanel.initPanelButtons(portalPanel.getPortletMatches());
		portalPanel.getPortletMatches().add(matchesPanel);

		// Spielerpanel
		playerPanel = new PlayerPanel();
		playerPanel.initPanelButtons(portalPanel.getPortletPlayer());
		portalPanel.getPortletPlayer().add(playerPanel);

		// Tabellenpanel
		tablePanel = new TablePanel();
		tablePanel.initPanelButtons(portalPanel.getPortletTable());
		portalPanel.getPortletTable().add(tablePanel);
	}

}
