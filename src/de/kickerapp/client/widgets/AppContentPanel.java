package de.kickerapp.client.widgets;

import com.sencha.gxt.widget.core.client.ContentPanel;

/**
 * Erweiterung des {@link ContentPanel}s für die Applikation.
 * 
 * @author Sebastian Filke
 */
public class AppContentPanel extends ContentPanel {

	/**
	 * Erzeugt einen neuen ContentPanel.
	 */
	public AppContentPanel() {
		super();
	}

	/**
	 * Erzeugt einen neuen ContentPanel mit übergebenem Aussehen.
	 * 
	 * @param appearance Das Aussehen des ContentPanels.
	 */
	public AppContentPanel(ContentPanelAppearance appearance) {
		super(appearance);
	}

}
