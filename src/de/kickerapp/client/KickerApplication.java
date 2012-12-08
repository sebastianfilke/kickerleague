package de.kickerapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.Viewport;

import de.kickerapp.client.ui.MainPanel;

/**
 * Hauptklasse welche die Applikation initalisiert und beim Start aufgerufen
 * wird.
 * 
 * @author Sebastian Filke
 */
public class KickerApplication implements IsWidget, EntryPoint {

	@Override
	public Widget asWidget() {
		return new MainPanel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onModuleLoad() {
		DOM.removeChild(RootPanel.getBodyElement(), DOM.getElementById("loading"));

		Viewport viewport = new Viewport();
		viewport.add(asWidget(), new MarginData(20, 40, 20, 40));
		RootPanel.get().add(viewport, 0, 0);
	}

}
