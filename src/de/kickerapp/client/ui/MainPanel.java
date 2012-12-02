package de.kickerapp.client.ui;

/**
 * Basis-Controller zur Darstellung und Verarbeitung der Applikation.
 * 
 * @author Sebastian Filke
 */
public class MainPanel extends BasePanel {

	private ResultPanel resultPanel;

	public MainPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	public void initLayout() {
		setHeadingHtml("<span style='font-size:18px;'>Kickerapplikation</i>");
		setBodyBorder(false);

		final PortalPanel portalPanel = new PortalPanel();
		initPanels(portalPanel);

		add(portalPanel);
	}

	private void initPanels(PortalPanel portalPanel) {
		resultPanel = new ResultPanel();
		portalPanel.getPortletResult().add(resultPanel);
	}

}
