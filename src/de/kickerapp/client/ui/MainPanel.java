package de.kickerapp.client.ui;

import de.kickerapp.client.ui.images.KickerIcons;
import de.kickerapp.client.ui.resources.KickerMessages;

/**
 * Basis-Controller zur Darstellung und Verarbeitung der Applikation.
 * 
 * @author Sebastian Filke
 */
public class MainPanel extends BasePanel {

	private ResultPanel resultPanel;

	private MatchesPanel matchesPanel;

	private PlayerPanel playerPanel;

	public MainPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		setHeadingHtml("<span style='font-size:18px;'>" + KickerMessages.MAIN_PANEL.mainPanelTitle() + "</i>");
		getHeader().setIcon(KickerIcons.ICON.socerBall());
		setBodyBorder(false);

		final PortalPanel portalPanel = new PortalPanel();
		initPanels(portalPanel);

		add(portalPanel);
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
		portalPanel.getPortletMatches().add(matchesPanel);

		// Spielerpanel
		playerPanel = new PlayerPanel();
		playerPanel.initPanelButtons(portalPanel.getPortletPlayer());
		portalPanel.getPortletPlayer().add(playerPanel);
	}

}
