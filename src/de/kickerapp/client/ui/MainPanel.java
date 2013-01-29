package de.kickerapp.client.ui;

import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;

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

		final Label lTopic = new Label(KickerMessages.MAIN_PANEL.mainPanelTitle());
		lTopic.addStyleName("headerLabel");

		final AppContentPanel cpWest = new AppContentPanel();

		final BorderLayoutContainer con = new BorderLayoutContainer();
		con.setBorders(true);

		final BorderLayoutData westData = new BorderLayoutData(280);
		westData.setCollapsible(true);
		westData.setSplit(true);
		westData.setCollapseMini(true);
		westData.setMargins(new Margins(0, 5, 0, 5));

		final BorderLayoutData northData = new BorderLayoutData(60);

		final PortalPanel portalPanel = new PortalPanel();
		initPanels(portalPanel);

		con.setWestWidget(cpWest, westData);
		con.setCenterWidget(portalPanel);
		con.setNorthWidget(lTopic, northData);

		add(con);
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
