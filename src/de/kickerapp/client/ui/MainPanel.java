package de.kickerapp.client.ui;

import de.kickerapp.client.ui.resources.MessageProvider;
import de.kickerapp.client.widgets.AppContentPanel;

/**
 * Basis-Controller zur Darstellung und Verarbeitung der Applikation.
 * 
 * @author Sebastian Filke
 */
public class MainPanel extends AppContentPanel {

	private ResultPanel resultPanel;

	private MatchesPanel matchesPanel;

	public MainPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	public void initLayout() {
		setHeadingHtml("<span style='font-size:18px;'>" + MessageProvider.get().mainPanelTitle() + "</i>");
		setBodyBorder(false);

		final PortalPanel portalPanel = new PortalPanel();
		initPanels(portalPanel);

		add(portalPanel);
	}

	private void initPanels(PortalPanel portalPanel) {
		resultPanel = new ResultPanel();
		portalPanel.getPortletResult().add(resultPanel);
		matchesPanel = new MatchesPanel();
		portalPanel.getPortletMatches().add(matchesPanel);
	}

}
