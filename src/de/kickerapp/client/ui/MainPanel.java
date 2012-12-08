package de.kickerapp.client.ui;

import de.kickerapp.client.ui.images.KickerIcons;
import de.kickerapp.client.ui.resources.MessageProvider;

/**
 * Basis-Controller zur Darstellung und Verarbeitung der Applikation.
 * 
 * @author Sebastian Filke
 */
public class MainPanel extends BasePanel {

	private ResultPanel resultPanel;

	private MatchesPanel matchesPanel;

	public MainPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		setHeadingHtml("<span style='font-size:18px;'>" + MessageProvider.get().mainPanelTitle() + "</i>");
		getHeader().setIcon(KickerIcons.GET.socerBall());
		setBodyBorder(false);

		final PortalPanel portalPanel = new PortalPanel();
		initPanels(portalPanel);

		add(portalPanel);
	}

	private void initPanels(PortalPanel portalPanel) {
		resultPanel = new ResultPanel();
		resultPanel.initPanelButtons(portalPanel.getPortletResult());
		portalPanel.getPortletResult().add(resultPanel);
		matchesPanel = new MatchesPanel();
		portalPanel.getPortletMatches().add(matchesPanel);
	}

}
