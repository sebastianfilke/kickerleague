package de.kickerapp.client.ui;

import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;

import de.kickerapp.client.widgets.AppButton;

public class CardPanel extends BasePanel {

	/**
	 * Erzeugt einen neuen Controller zum Eintragen neuer Spieler für die
	 * Applikation.
	 */
	public CardPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		super.initLayout();
		setHeaderVisible(true);

		add(createButtons(), new MarginData(5));
	}

	private VBoxLayoutContainer createButtons() {
		final VBoxLayoutContainer vlcButtons = new VBoxLayoutContainer();
		vlcButtons.setPadding(new Padding(5));
		vlcButtons.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);

		vlcButtons.add(new AppButton("Tabelle"), new BoxLayoutData(new Margins(0, 0, 10, 0)));
		vlcButtons.add(new AppButton("Ergebnisse"), new BoxLayoutData(new Margins(0, 0, 10, 0)));
		vlcButtons.add(new AppButton("Spiel eintragen"), new BoxLayoutData(new Margins(0, 0, 10, 0)));
		vlcButtons.add(new AppButton("Spieler"), new BoxLayoutData(new Margins(0, 0, 10, 0)));

		return vlcButtons;
	}
	
}
